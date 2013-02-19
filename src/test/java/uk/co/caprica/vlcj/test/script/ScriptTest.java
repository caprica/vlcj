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
 * Copyright 2009, 2010, 2011, 2012, 2013 Caprica Software Limited.
 */

package uk.co.caprica.vlcj.test.script;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import uk.co.caprica.vlcj.Info;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.test.VlcjTest;

/**
 * A test to show how to integrate J2SE scripting.
 * <p>
 * There is nothing special in this test that is required for vlcj as compared to
 * any other J2SE scripting example - other than vlcj objects are made available
 * to the scripting engine.
 * <p>
 * This test application can be used to perform ad-hoc tests on vlcj components
 * if there is no dedicated test example.
 * <p>
 * Only the selected text is executed, and to execute you press CTRL+ENTER after
 * you have made your selection.
 * <p>
 * Some examples are provided in the input window, but you are free to edit this
 * to execute any code.
 * <p>
 * You can clear the output window with CTRL+DELETE when it is focused.
 */
public class ScriptTest extends VlcjTest {

    private final JFrame mainFrame;
    private final JTextArea scriptTextArea;
    private final JTextArea outputTextArea;

    private final JFrame eventFrame;
    private final JTextArea eventTextArea;

    private final ScriptEngineManager scriptEngineManager;
    private final ScriptEngine scriptEngine;

    private final MediaPlayerFactory mediaPlayerFactory;
    private final MediaPlayer mediaPlayer;

    public static void main(String[] args) throws ScriptException {
        new ScriptTest().start();
    }

    public ScriptTest() {
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());

        Font font = new Font("Monospaced", Font.PLAIN, 12);

        scriptTextArea = new JTextArea();
        scriptTextArea.setFont(font);

        outputTextArea = new JTextArea();
        outputTextArea.setFont(font);
        outputTextArea.setEditable(false);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        JScrollPane scriptScrollPane = new JScrollPane(scriptTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        splitPane.setTopComponent(scriptScrollPane);

        JScrollPane outputScrollPane = new JScrollPane(outputTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        splitPane.setBottomComponent(outputScrollPane);

        contentPane.add(splitPane, BorderLayout.CENTER);

        splitPane.setDividerLocation(700);

        mainFrame = new JFrame("vlcj scripting");
        mainFrame.setSize(1600, 1000);
        mainFrame.setContentPane(contentPane);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel eventContentPane = new JPanel();
        eventContentPane.setLayout(new BorderLayout());

        eventTextArea = new JTextArea();
        eventTextArea.setFont(font);
        eventTextArea.setEditable(false);

        JScrollPane eventScrollPane = new JScrollPane(eventTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        eventContentPane.add(eventScrollPane, BorderLayout.CENTER);

        eventFrame = new JFrame("vlcj events");
        eventFrame.setSize(600, 200);
        eventFrame.setContentPane(eventContentPane);
        eventFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        scriptEngineManager = new ScriptEngineManager();
        scriptEngine = scriptEngineManager.getEngineByMimeType("application/javascript");

        mediaPlayerFactory = new MediaPlayerFactory();
        mediaPlayer = mediaPlayerFactory.newHeadlessMediaPlayer();

        mediaPlayer.addMediaPlayerEventListener(new MediaPlayerEventHandler());

        scriptEngine.put("vlcj", Info.getInstance().version());
        scriptEngine.put("mediaPlayerFactory", mediaPlayerFactory);
        scriptEngine.put("mediaPlayer", mediaPlayer);

        // Add some examples (not exhaustive by any means)
        scriptTextArea.append("vlcj\n");
        scriptTextArea.append("\n");

        scriptTextArea.append("mediaPlayerFactory.version()\n");
        scriptTextArea.append("mediaPlayerFactory.changeset()\n");
        scriptTextArea.append("mediaPlayerFactory.compiler()\n");
        scriptTextArea.append("mediaPlayerFactory.getAudioFilters()\n");
        scriptTextArea.append("mediaPlayerFactory.getVideoFilters()\n");
        scriptTextArea.append("mediaPlayerFactory.getAudioOutputs()\n");
        scriptTextArea.append("\n");

        scriptTextArea.append("mediaPlayer.playMedia(\"<filename>\", null)\n");
        scriptTextArea.append("mediaPlayer.startMedia(\"<filename>\", null)\n");
        scriptTextArea.append("mediaPlayer.play()\n");
        scriptTextArea.append("mediaPlayer.setPosition(0.2)\n");
        scriptTextArea.append("mediaPlayer.setTime(30)\n");
        scriptTextArea.append("mediaPlayer.pause()\n");
        scriptTextArea.append("mediaPlayer.stop()\n");
        scriptTextArea.append("mediaPlayer.start()\n");
        scriptTextArea.append("mediaPlayer.getMediaDetails()\n");
        scriptTextArea.append("mediaPlayer.getVideoDimension()\n");
        scriptTextArea.append("mediaPlayer.getLength()\n");
        scriptTextArea.append("mediaPlayer.getChapterCount()\n");
        scriptTextArea.append("mediaPlayer.getChapter()\n");
        scriptTextArea.append("mediaPlayer.setChapter(<n>)\n");
        scriptTextArea.append("mediaPlayer.previousChapter()\n");
        scriptTextArea.append("mediaPlayer.nextChapter()\n");
        scriptTextArea.append("mediaPlayer.setVolume(<n>)\n");
        scriptTextArea.append("mediaPlayer.getVolume()\n");
        scriptTextArea.append("mediaPlayer.mute(true)\n");
        scriptTextArea.append("mediaPlayer.mute(false)\n");
        scriptTextArea.append("mediaPlayer.isMute()\n");
        scriptTextArea.append("\n");
        scriptTextArea.append("mediaPlayer.getTrackInfo()\n");
        scriptTextArea.append("mediaPlayer.getTitleCount()\n");
        scriptTextArea.append("mediaPlayer.getVideoTrackCount()\n");
        scriptTextArea.append("mediaPlayer.getAudioTrackCount()\n");
        scriptTextArea.append("mediaPlayer.getSpuCount()\n");
        scriptTextArea.append("mediaPlayer.getTitleDescriptions()\n");
        scriptTextArea.append("mediaPlayer.getVideoDescriptions()\n");
        scriptTextArea.append("mediaPlayer.getAudioDescriptions()\n");
        scriptTextArea.append("mediaPlayer.getSpuDescriptions()\n");
        scriptTextArea.append("mediaPlayer.getChapterDescriptions(1)\n");
        scriptTextArea.append("mediaPlayer.getChapterDescriptions()\n");
        scriptTextArea.append("mediaPlayer.getAllChapterDescriptions()\n");
        scriptTextArea.append("mediaPlayer.getTitle()\n");
        scriptTextArea.append("mediaPlayer.setTitle(1)\n");
        scriptTextArea.append("mediaPlayer.getVideoTrack()\n");
        scriptTextArea.append("mediaPlayer.setVideoTrack(1)\n");
        scriptTextArea.append("mediaPlayer.getAudioTrack()\n");
        scriptTextArea.append("mediaPlayer.setAudioTrack(1)\n");
        scriptTextArea.append("mediaPlayer.getSpu()\n");
        scriptTextArea.append("mediaPlayer.setSpu(1)\n");

        scriptTextArea.append("\n");

        scriptTextArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if(evt.getKeyCode() == KeyEvent.VK_ENTER && (evt.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0) {
                    String selected = scriptTextArea.getSelectedText();
                    if(selected == null) {
                        return;
                    }
                    try {
                        outputTextArea.append(selected);
                        outputTextArea.append("\n");
                        Object result = scriptEngine.eval(selected);
                        if(result instanceof Iterable) {
                            Iterable<?> it = (Iterable<?>)result;
                            for(Object obj : it) {
                                outputTextArea.append("-> ");
                                outputTextArea.append(String.valueOf(obj));
                                outputTextArea.append("\n");
                            }
                        }
                        else if(result instanceof Map) {
                            @SuppressWarnings("unchecked")
                            Map<Object,Object> map = (Map<Object,Object>)result;
                            for(Map.Entry<Object, Object> entry : map.entrySet()) {
                                outputTextArea.append("-> ");
                                outputTextArea.append(String.format("%30s ---> %s%n", entry.getKey(), entry.getValue()));
                            }
                        }
                        else {
                            outputTextArea.append("-> ");
                            outputTextArea.append(String.valueOf(result));
                            outputTextArea.append("\n");
                        }
                        outputTextArea.append("\n");
                        outputTextArea.setCaretPosition(outputTextArea.getText().length() - 1);
                    }
                    catch(ScriptException ex) {
                        outputTextArea.append("-> ");
                        outputTextArea.append(ex.getMessage());
                        outputTextArea.append("\n");
                        ex.printStackTrace();
                    }
                }
            }
        });

        outputTextArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if(evt.getKeyCode() == KeyEvent.VK_DELETE && (evt.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0) {
                    outputTextArea.setText("");
                }
            }
        });

        eventTextArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                if(evt.getKeyCode() == KeyEvent.VK_DELETE && (evt.getModifiersEx() & InputEvent.CTRL_DOWN_MASK) != 0) {
                    eventTextArea.setText("");
                }
            }
        });
    }

    private void start() {
        mainFrame.setVisible(true);
        eventFrame.setVisible(true);
    }

    private final class MediaPlayerEventHandler extends MediaPlayerEventAdapter {

        private final DateFormat df = new SimpleDateFormat("HH:mm:ss.sss");

        @Override
        public void opening(MediaPlayer mediaPlayer) {
            event("opening");
        }

        @Override
        public void buffering(MediaPlayer mediaPlayer, float newCache) {
            event(String.format("buffering %.0f", newCache));
        }

        @Override
        public void playing(MediaPlayer mediaPlayer) {
            event("playing");
        }

        @Override
        public void paused(MediaPlayer mediaPlayer) {
            event("paused");
        }

        @Override
        public void stopped(MediaPlayer mediaPlayer) {
            event("stopped");
        }

        @Override
        public void finished(MediaPlayer mediaPlayer) {
            event("finished");
        }

        @Override
        public void error(MediaPlayer mediaPlayer) {
            event("error");
        }

        @Override
        public void newMedia(MediaPlayer mediaPlayer) {
            event("newMedia");
        }

        private void event(String msg) {
            eventTextArea.append(df.format(new Date()));
            eventTextArea.append(" ");
            eventTextArea.append(msg);
            eventTextArea.append("\n");
            eventTextArea.setCaretPosition(eventTextArea.getText().length() - 1);
        }
    }
}
