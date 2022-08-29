package com.company.client.console;

import com.company.common.ClientType;
import com.company.client.utils.ConsoleUtils;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

public class ClientTypeChoosingSupplier implements Supplier<ClientType> {

    @Override
    public ClientType get() {
        while (true) {
            StringBuilder builder = new StringBuilder();

            builder.append("Choose application type: \n");

            AtomicInteger counter = new AtomicInteger(0);
            Arrays.stream(ClientType.values())
                    .map(x -> String.format("%s. %s\n", counter.incrementAndGet(), x.name()))
                    .forEach(builder::append);

            builder.append("Or 'exit' to quit");
            String response = ConsoleUtils.getInput(builder.toString());
            if (response.equals("exit")) {
                System.exit(0);
                return null;
            }

            try {
                int value = Integer.parseInt(response);
                var clientType = ClientType.findByTypeId(value);
                if (clientType == null) {
                    System.err.println("Client type is not valid");
                    System.out.println();
                    continue;
                }

                return clientType;
            } catch (NumberFormatException ex) {
                System.err.println("Option chosen is not a number");
                System.out.println();
            }
        }
    }
}
