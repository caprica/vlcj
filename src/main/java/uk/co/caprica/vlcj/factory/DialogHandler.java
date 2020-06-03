/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009-2019 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.factory;

/**
 * Specification for a component that handles native dialogs.
 * <p>
 * A dialog need <em>not</em> have a user-interface. For example, a login dialog could load credentials from a file or
 * system properties and programmatically deal with the dialog.
 * <p>
 * Implementations should use {@link MediaPlayerFactory#dialogs()} to interact with the dialogs.
 */
public interface DialogHandler {

    /**
     * Present an error dialog.
     *
     * @param userData user data
     * @param title dialog title
     * @param text error text
     */
    void displayError(Long userData, String title, String text);

    /**
     * Present a login dialog.
     *
     * @param userData user data
     * @param id dialog id, used to interact with this dialog
     * @param title dialog title
     * @param text login text
     * @param defaultUsername default username to display in the dialog
     * @param askStore if <code>true</code>, ask if the credentials should be stored
     */
    void displayLogin(Long userData, DialogId id, String title, String text, String defaultUsername, boolean askStore);

    /**
     * Present a question dialog.
     *
     * @param userData user data
     * @param id dialog id, used to interact with this dialog
     * @param title dialog title
     * @param text question text
     * @param type type of question
     * @param cancel cancel action text
     * @param action1 first action text
     * @param action2 second action text
     */
    void displayQuestion(Long userData, DialogId id, String title, String text, DialogQuestionType type, String cancel, String action1, String action2);

    /**
     * Present a progress dialog.
     *
     * @param userData user data
     * @param id dialog id, used to interact with this dialog
     * @param title dialog title
     * @param text progress text
     * @param indeterminate <code>true</code> if the progress is indeterminate; <code>false</code> if it is not
     * @param position percent completion
     * @param cancel cancel action text
     */
    void displayProgress(Long userData, DialogId id, String title, String text, int indeterminate, float position, String cancel);

    /**
     * Present a cancel dialog.
     *
     * @param userData user data
     * @param id dialog id, used to interact with this dialog
     */
    void cancel(Long userData, DialogId id);

    /**
     * Update a progress dialog.
     *
     * @param userData user data
     * @param id dialog id, used to interact with this dialog
     * @param position percent completion
     * @param text progress text
     */
    void updateProgress(Long userData, DialogId id, float position, String text);

}
