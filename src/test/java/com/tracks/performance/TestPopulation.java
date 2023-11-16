package com.tracks.performance;

import io.gatling.javaapi.core.*;

import static com.tracks.performance.enums.RuntimeProperties.*;
import static io.gatling.javaapi.core.CoreDsl.*;

public class TestPopulation {

    private static final Integer numberOfUsersPerSecondOnPlato = NUMBER_OF_USERS_PER_SECOND_ON_PLATO.getIntegerValue();
    private static final Integer platoLength = PLATO_LENGTH.getIntegerValue();
    private static final Integer stepsLength = STEP_LENGTH.getIntegerValue();
    private static final Integer highestStepRps = HIGHEST_STEP_RPS.getIntegerValue();
    private static final Integer rpsOnPlato = RPS_ON_PLATO.getIntegerValue();
    private static final Integer peakRps = PEAK_RPS.getIntegerValue();
    private static final Integer timeBetweenPeak = TIME_BETWEEN_PEAKS.getIntegerValue();

    /*
    Plato's user population profile provide the constant number of users that are injected to application per second.
    In the beginning it wait 5 sec with no actions. The get to defined by user number of users per seconds during 5 sec.
    Hold it during time that is defined by user (platoLength). In the end it gets to 1 user per second and holds it
    during 5 sec.
     */
    public static OpenInjectionStep[] getPlatoUserPopulation() {
        return new OpenInjectionStep[] {
                nothingFor(5),
                rampUsersPerSec(1).to(numberOfUsersPerSecondOnPlato).during(5),
                constantUsersPerSec(numberOfUsersPerSecondOnPlato).during(platoLength),
                rampUsersPerSec(numberOfUsersPerSecondOnPlato).to(1).during(5),
                constantUsersPerSec(1).during(5)
        };
    }

    // Users as an origin of requests for RPS population profile
    public static OpenInjectionStep[] getAtOnceUsersPopulation() {
        return new OpenInjectionStep[] {
                atOnceUsers(5000)
        };
    }

    /*
    Steps population profile is being created by increasing of requests per second step by step. The first step is one
    fourth of highest RPS value that is defined by user (highestStepRps). The second step is the half of highest RPS and
    finally third step is highestStepRps properly. Every step holds during time that is defined by user (stepsLength).
    In the end it gets 1 RPS and holds it for 5 sec.
     */
    public static ThrottleStep[] getStepsRpsPopulation() {
        return new ThrottleStep[] {
                reachRps(highestStepRps / 4).in(1),
                holdFor(stepsLength),
                reachRps(highestStepRps / 2).in(1),
                holdFor(stepsLength),
                reachRps(highestStepRps).in(1),
                holdFor(stepsLength),
                jumpToRps(1),
                holdFor(5)
        };
    }

    // The similar profile as Plato's user population profile. RPS is instead users per second
    public static ThrottleStep[] getPlatoRpsPopulation() {
        return new ThrottleStep[] {
                jumpToRps(rpsOnPlato),
                holdFor(platoLength),
                jumpToRps(1),
                holdFor(5)
        };
    }

    /*
    Spiked RPS population profile provides a constant 1 RPS with spikes that is defined by the user (peakRps).
    The periodicity of spikes is defined by the user also (timeBetweenPeak)
     */
    public static ThrottleStep[] getSpikedRpsPopulation() {
        return new ThrottleStep[] {
                jumpToRps(1),
                holdFor(timeBetweenPeak),
                jumpToRps(peakRps),
                holdFor(1),
                jumpToRps(1),
                holdFor(timeBetweenPeak),
                jumpToRps(peakRps),
                holdFor(1),
                jumpToRps(1),
                holdFor(timeBetweenPeak)
        };
    }
}
