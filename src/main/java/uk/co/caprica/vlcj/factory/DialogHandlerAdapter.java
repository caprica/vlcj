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
 * Copyright 2009-2021 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.factory;

/**
 * Empty implementation of a {@link DialogHandler}.
 * <p>
 * These empty implementations are useless by themselves, sub-classes provide the necessary functionality for the
 * dialogs of interest.
 */
abstract public class DialogHandlerAdapter implements DialogHandler {

    @Override
    public void displayError(Long userData, String title, String text) {
    }

    @Override
    public void displayLogin(Long userData, DialogId id, String title, String text, String defaultUsername, boolean askStore) {
    }

    @Override
    public void displayQuestion(Long userData, DialogId id, String title, String text, DialogQuestionType type, String cancel, String action1, String action2) {
    }

    @Override
    public void displayProgress(Long userData, DialogId id, String title, String text, int indeterminate, float position, String cancel) {
    }

    @Override
    public void cancel(Long userData, DialogId id) {
    }

    @Override
    public void updateProgress(Long userData, DialogId id, float position, String text) {
    }
}
