/*
 * This file is part of adventure, licensed under the MIT License.
 *
 * Copyright (c) 2020 KyoriPowered
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.kyori.adventure.platform.fabric.mixin;

import java.util.Collection;
import java.util.Collections;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.fabric.AdventureCommandSource;
import net.kyori.adventure.platform.fabric.CommandOutputAudience;
import net.kyori.adventure.platform.fabric.FabricPlatform;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * The methods in this class should match the implementations of their Text-using counterparts in {@link ServerCommandSource}
 *
 */
@Mixin(ServerCommandSource.class)
public abstract class MixinServerCommandSource implements AdventureCommandSource {
  @Shadow @Final private CommandOutput output;
  @Shadow @Final private boolean silent;

  @Shadow protected abstract void sendToOps(Text text);

  private @MonotonicNonNull Audience out;
  private @MonotonicNonNull Collection<Audience> ownOut;

  @Override
  public void sendFeedback(final Component text, final boolean sendToOps) {
    if(this.output.shouldReceiveFeedback() && !this.silent) {
      this.out.sendMessage(text);
    }

    if(sendToOps && this.output.shouldBroadcastConsoleToOps() && !this.silent) {
      this.sendToOps(FabricPlatform.adapt(text));
    }
  }

  @Override
  public void sendError(final Component text) {
    if(this.output.shouldTrackOutput()) {
      this.out.sendMessage(text.color(NamedTextColor.RED));
    }
  }

  @Override
  public @NonNull Iterable<? extends Audience> audiences() {
    if(this.ownOut == null) {
      this.ownOut = Collections.singleton(this.out = CommandOutputAudience.of(this.output));
    }
    return this.ownOut;
  }
}
