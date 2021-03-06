/*
Copyright 2019 Amazon.com, Inc. or its affiliates. All Rights Reserved.
Licensed under the Apache License, Version 2.0 (the "License").
You may not use this file except in compliance with the License.
A copy of the License is located at
    http://www.apache.org/licenses/LICENSE-2.0
or in the "license" file accompanying this file. This file is distributed
on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
express or implied. See the License for the specific language governing
permissions and limitations under the License.
*/

package com.amazonaws.services.neptune.io;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public enum Target {
    files {
        @Override
        public OutputWriter createOutputWriter(Path filePath, KinesisConfig kinesisConfig) throws IOException {
            return new PrintOutputWriter(new FileWriter(filePath.toFile()));
        }

        @Override
        public void writeCommandResult(Object result) {
            System.out.println(result);
        }
    },
    stdout {
        @Override
        public OutputWriter createOutputWriter(Path filePath, KinesisConfig kinesisConfig) {
            return new PrintOutputWriter(System.out);
        }

        @Override
        public void writeCommandResult(Object result) {
            System.err.println(result);
        }
    },
    stream {
        @Override
        public OutputWriter createOutputWriter(Path filePath, KinesisConfig kinesisConfig) throws IOException {

            return new FileToStreamOutputWriter(
                    new KinesisStreamPrintOutputWriter(new FileWriter(filePath.toFile())),
                    filePath,
                    kinesisConfig);
        }

        @Override
        public void writeCommandResult(Object result) {
            System.out.println(result);
        }
    };

    public abstract OutputWriter createOutputWriter(Path filePath, KinesisConfig kinesisConfig) throws IOException;

    public abstract void writeCommandResult(Object result);

}
