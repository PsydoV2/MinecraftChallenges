package me.psydo.challengesCore;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ChallengeRegistry {
    private final Map<String, IChallenge> challenges = new HashMap<>();

    public void registerChallenge(IChallenge challenge) {
        challenges.put(challenge.getChallengeId().toLowerCase(), challenge);
    }

    public IChallenge getChallenge(String name) {
        return challenges.get(name.toLowerCase());
    }

    public Set<String> getChallengeNames() {
        return challenges.keySet();
    }
}
