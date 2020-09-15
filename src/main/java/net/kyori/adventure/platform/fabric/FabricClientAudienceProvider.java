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

package net.kyori.adventure.platform.fabric;

import java.util.Locale;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.fabric.impl.client.FabricClientAudienceProviderImpl;
import net.kyori.adventure.text.renderer.ComponentRenderer;
import org.checkerframework.checker.nullness.qual.NonNull;

import static java.util.Objects.requireNonNull;

/**
 * Access the client's player as an {@link net.kyori.adventure.audience.Audience}
 */
public interface FabricClientAudienceProvider extends FabricAudiences {

  /**
   * Get the common instance, that will render using the global translation registry.
   *
   * @return the audience provider
   */
  static FabricClientAudienceProvider of() {
    return FabricClientAudienceProviderImpl.INSTANCE;
  }

  /**
   * Get an audience provider that will render using the provided renderer.
   *
   * @param renderer Renderer to use
   * @return new audience provider
   */
  static FabricClientAudienceProvider of(final ComponentRenderer<Locale> renderer) {
    return new FabricClientAudienceProviderImpl(requireNonNull(renderer, "renderer"));
  }

  /**
   * Get an audience for the client's player.
   *
   * <p>When not in-game, most operations will no-op</p>
   *
   * @return player audience
   */
  @NonNull Audience audience();
}
