package com.supersouper.whichery.api.rituals;

public interface IRitualParticipator {

    void ritualTick();

    void transitionToStage(int stage);
}
