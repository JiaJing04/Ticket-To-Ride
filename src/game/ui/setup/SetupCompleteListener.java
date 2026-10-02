package game.ui.setup;


import game.setup.GameConfiguration;

/**
 * Listener for setup completion events.
 *
 * @author Hew Jia Jing
 */
public interface SetupCompleteListener {

    /**
     * Handles completion of the setup process.
     *
     * @param configuration the finalized game configuration
     */
    void onSetupComplete(GameConfiguration configuration);
}
