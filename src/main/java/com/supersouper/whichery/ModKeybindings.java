package com.supersouper.whichery;

import org.lwjgl.input.Keyboard;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;
import com.supersouper.whichery.common.event.VampirismEvents;

public class ModKeybindings {

    public static SyncedKeybind VAMPIRE_BITE_KEY;
    public static SyncedKeybind VAMPIRE_TRANSFORM_KEY;

    public static void init() {
        VAMPIRE_BITE_KEY = SyncedKeybind
            .createConfigurable("whichery.key.vampire_bite", "whichery.key.categories.whichery", Keyboard.KEY_V)
            .registerGlobalListener(VampirismEvents::bitePressed);
        VAMPIRE_TRANSFORM_KEY = SyncedKeybind
            .createConfigurable("whichery.key.vampire_transform", "whichery.key.categories.whichery", Keyboard.KEY_R)
            .registerGlobalListener(VampirismEvents::transformPressed);
    }
}
