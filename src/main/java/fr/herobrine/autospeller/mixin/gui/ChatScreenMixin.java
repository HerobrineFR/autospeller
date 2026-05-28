package fr.herobrine.autospeller.mixin.gui;

import dev.kikugie.fletching_table.annotation.MixinEnvironment;
import fr.herobrine.autospeller.client.AutospellerClient;
import fr.herobrine.autospeller.client.linting.ChatLintingSession;
import fr.herobrine.autospeller.client.rendering.ChatRenderer;
import fr.herobrine.autospeller.client.rendering.ChatRenderingTicket;
import fr.herobrine.autospeller.language.TokenInputElement;
import fr.herobrine.autospeller.linting.LintingResult;
import fr.herobrine.autospeller.linting.LintingTicket;
import kotlin.time.Clock;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.input.MouseButtonEvent;
import org.joml.Vector2i;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.CompletableFuture;

@MixinEnvironment(type = MixinEnvironment.Env.CLIENT)
@Mixin(ChatScreen.class)
public class ChatScreenMixin {

    @Shadow
    protected EditBox input;

    @Shadow
    protected boolean isDraft;

    @Unique @Nullable
    private ChatRenderingTicket chatRenderingTicket;

    @Unique @Nullable
    private ChatLintingSession lintingSession;

    @Unique @Nullable
    private CompletableFuture<LintingResult> lintingTask = null;

    @Unique
    private ChatRenderer.TooltipWidget selectedSuggestion = null;

    @Inject(
            method = {"extractRenderState"},
            at = {@At("TAIL")}
    )
    private void onRender(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, CallbackInfo ci) {
        var lintingService = AutospellerClient.INSTANCE.getService();

        if(lintingService.getLinterConfiguration().getEnableMod() && input.isVisible()) {
            this.initializeSession();

            if(!lintingService.getInputProcessor().isReady()) {
                ChatRenderer.INSTANCE.displayInputProcessorPendingState(
                        graphics
                );

                return;
            }

            if(this.isTaskAvailable()) {
                var textInput = input.getValue();
                var lintingDebounce = lintingService.getDebounce();
                var lintingTicket = new LintingTicket(
                        new TokenInputElement(textInput),
                        lintingDebounce
                );

                if(this.lintingSession.shouldLint(lintingTicket)) {
                    this.lintingTask = lintingService.processTicket(lintingTicket, lintingSession);

                    this.lintingTask.thenAcceptAsync(result -> {
                        this.chatRenderingTicket = new ChatRenderingTicket(
                                this.lintingSession,
                                result.getTextSuggestions()
                        );
                    });
                }
            }

            if(this.chatRenderingTicket != null) {
                this.selectedSuggestion = ChatRenderer.INSTANCE.submitUnderlinesTicket(
                        graphics,
                        this.chatRenderingTicket,
                        new Vector2i(mouseX, mouseY),
                        !this.isDraft
                );
            }
        }
    }

    @Inject(
            method = "mouseClicked",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onClick(MouseButtonEvent event, boolean doubleClick, CallbackInfoReturnable<Boolean> cir) {
        if(selectedSuggestion != null) {
            ChatRenderer.INSTANCE.replaceInput(
                    this.selectedSuggestion,
                    this.input
            );

            cir.cancel();
        }
    }

    private boolean isTaskAvailable() {
        return this.lintingTask == null || this.lintingTask.isDone();
    }

    private void initializeSession() {
        if(this.lintingSession == null || this.lintingSession.getEditBox() != this.input) {
            this.lintingSession = new ChatLintingSession(
                    Clock.System.INSTANCE.now(), null, this.input
            );
        }
    }
}
