package com.tracks.performance.chains;

import com.tracks.performance.utils.InputDataGenerator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class BaseChain {

    public final static String CONTEXT_NAME = "contextName";
    public final static String CONTEXT_STATE = "contextState";
    //FEEDER
    protected static Iterator<Map<String, Object>> customFeeder = Stream.generate((Supplier<Map<String, Object>>)
        () -> new HashMap<>(Map.of(
                CONTEXT_NAME, InputDataGenerator.generateRandomName(),
                CONTEXT_STATE, InputDataGenerator.generateRandomStatus()
        ))).iterator();
}
