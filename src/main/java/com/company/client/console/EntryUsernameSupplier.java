package com.company.client.console;

import com.company.client.utils.ConsoleUtils;

import java.util.function.Supplier;

public class EntryUsernameSupplier implements Supplier<String> {

    @Override
    public String get() {
        return ConsoleUtils.getInput("\nEnter your name: ");
    }
}
