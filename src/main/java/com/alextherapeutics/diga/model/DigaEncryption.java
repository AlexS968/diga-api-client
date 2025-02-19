/*
 * Copyright 2021-2021 Alex Therapeutics AB and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

package com.alextherapeutics.diga.model;

import de.tk.opensource.secon.SECON;
import de.tk.opensource.secon.SeconException;
import de.tk.opensource.secon.Subscriber;
import lombok.Builder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Attempt to encrypt an inputstream using SECON
 */
@Builder
@Slf4j
public class DigaEncryption {
    /**
     * The encrypting subscriber
     */
    @NonNull
    private Subscriber subscriber;
    /**
     * The input to encrypt
     */
    @NonNull
    private byte[] encryptionTarget;
    /**
     * The alias of the key in the public key directory
     */
    @NonNull
    private String recipientAlias;

    /**
     * Encrypt the contents as a byte array output stream
     * @return
     */
    public ByteArrayOutputStream encrypt() throws IOException, SeconException {
        try (var input = new ByteArrayInputStream(encryptionTarget)) {
            try (var output = new ByteArrayOutputStream()) {
                SECON.copy(
                        () -> new ByteArrayInputStream(input.readAllBytes()),
                        subscriber.signAndEncryptTo(
                                () -> output, recipientAlias
                        )
                );
                return output;
            }
        }
    }
}
