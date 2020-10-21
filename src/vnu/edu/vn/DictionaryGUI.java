package vnu.edu.vn;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;

import static vnu.edu.vn.Dictionary.*;
import static vnu.edu.vn.GoogleAPI.GoogleTrans;
import static vnu.edu.vn.GoogleAPI.VoiceSpeak;

public class DictionaryGUI {
    private DictionaryManagement DM = new DictionaryManagement();

    private JPanel UETDictionary;
    private JTextField searchField;
    private JButton searchButton;
    private JList<String> list;
    private JButton speakButton;
    private JTextPane textPane;
    private JButton insButton;
    private JButton delButton;
    private JToggleButton updButton;
    private JButton exitButton;
    private JScrollPane Pane1;
    private JScrollPane Pane2;
    private JLabel logo;
    private JButton saveButton;

    public DictionaryGUI() throws SQLException, ClassNotFoundException {
        createIcon();
        DM.insertFromMySQL();
        DM.dictionarySearcher();

        textPane.setEditable(false);
        insButton.setVisible(false);
        updButton.setVisible(false);
        delButton.setVisible(false);
        saveButton.setVisible(false);

        insButton.addActionListener(new InsBtnClicked());
        delButton.addActionListener(new DelBtnClicked());
        updButton.addActionListener(new UpdBtnClicked());
        exitButton.addActionListener(new ExitBtnClicked());

        searchButton.addActionListener(new SearchBtnClicked());
        speakButton.addActionListener(new SpeakBtnClicked());
        searchField.addKeyListener(new SearchFldInputed());
        saveButton.addActionListener(new UpdWord());

        list.addListSelectionListener(new ListSelection());

        // set event enter của searchField = searchButton
        searchField.addActionListener(searchButton.getActionListeners()[0]);
    }

    private void setButton() {
        insButton.setVisible(false);
        updButton.setVisible(false);
        delButton.setVisible(false);

        if (ExplainOfSearchWord.equals(tipSearch)) {
            insButton.setVisible(true);
            updButton.setVisible(false);
            delButton.setVisible(false);
            return;
        }

        if (!ExplainOfSearchWord.equals("")) {
            insButton.setVisible(false);
            updButton.setVisible(true);
            delButton.setVisible(true);
            return;
        }
    }

    private void search() throws IOException {
        searchWord = searchField.getText();
        DM.dictionaryLookup();

        setButton();

        if (ExplainOfSearchWord.equals("") || ExplainOfSearchWord.equals(tipSearch)) {
            ExplainOfSearchWord = GoogleTrans(searchWord);
            if (ExplainOfSearchWord.equals("")) insButton.setVisible(false);
            else insButton.setVisible(true);
        }

        setTextPane(ExplainOfSearchWord);
    }

    //in ra gợi ý trên màn hình
    private void getHint() {
        ExplainOfSearchWord = "";
        setButton();

        searchWord = searchField.getText();

        if (!mapHint.containsKey(searchWord)) {
            ExplainOfSearchWord = tipSearch;
            list.setListData(new String[0]);
        }
        else {
            Collections.sort(mapHint.get(searchWord));
            String[] hintArray = new String[mapHint.get(searchWord).size()];
            hintArray = mapHint.get(searchWord).toArray(hintArray);

            String s = searchWord;
            list.setListData(hintArray);
            if (s != searchWord) searchField.setText(s);
        }

        Pane1.getViewport().add(list);
        Pane1.setVisible(true);

        setTextPane(ExplainOfSearchWord);
    }

    //set từ trong text pane
    private void setTextPane(String ExplainOfSearchWord) {
        textPane.setText(ExplainOfSearchWord);
        textPane.setCaretPosition(0);
        Pane2.getViewport().add(textPane);
        Pane2.setVisible(true);
    }

    //bắt event cho nút insert
    private class InsBtnClicked implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JPanel panel = new JPanel();
            int n = JOptionPane.showConfirmDialog(
                    panel,
                    "Are you sure you want to insert this word?",
                    "Insert Word",
                    JOptionPane.YES_NO_OPTION);
            if(n == JOptionPane.YES_OPTION) {
                DM.insertWord(searchField.getText(), textPane.getText());

                setButton();
                getHint();
                try {
                    search();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    //bắt event cho nút xóa
    private class DelBtnClicked implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JPanel panel = new JPanel();
            int n = JOptionPane.showConfirmDialog(
                    panel,
                    "Are you sure you want to delete this word?",
                    "Delete Word",
                    JOptionPane.YES_NO_OPTION);
            if (n == JOptionPane.YES_OPTION) {
                DM.deleteWord(searchField.getText());

                getHint();
            }
        }
    }
    //bắt event cho nút update
    private class UpdBtnClicked implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (updButton.isSelected()) {
                saveButton.setVisible(true);
                textPane.setEditable(true);
                searchField.setEditable(false);
                textPane.grabFocus();
                UETDictionary.getRootPane().setDefaultButton(saveButton);
            }
            else {
                saveButton.setVisible(false);
                textPane.setEditable(false);
                searchField.setEditable(true);
                searchField.grabFocus();
                try {
                    search();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    //bắt sự kiên cho nút save
    private class UpdWord implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JPanel panel = new JPanel();
            int n = JOptionPane.showConfirmDialog(
                    panel,
                    "Are you sure you want to update this word?",
                    "Update Word",
                    JOptionPane.YES_NO_OPTION);
            if(n == JOptionPane.YES_OPTION) {
                DM.updateWord(searchField.getText(), textPane.getText());
                updButton.setSelected(false);
                saveButton.setVisible(false);
                textPane.setEditable(false);
                searchField.setEditable(true);
                searchField.grabFocus();
            }
        }
    }

    //bắt sự kiện nút exit
    private class ExitBtnClicked implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            JPanel panel = new JPanel();
            int n = JOptionPane.showConfirmDialog(
                    panel,
                    "Are you sure you want to quit?",
                    "Alert",
                    JOptionPane.YES_NO_OPTION);
            if(n == JOptionPane.YES_OPTION)
                System.exit(0);
        }
    }

    //bắt sự kiện nút search
    private class SearchBtnClicked implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            getHint();
            try {
                search();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
    //bắt sự kiện speak
    private class SpeakBtnClicked implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            VoiceSpeak(searchWord);
        }
    }

    //nhập từ cần search*
    private  class SearchFldInputed extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            super.keyPressed(e);

            if (e.getKeyCode() == KeyEvent.VK_ENTER) return;

            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                list.grabFocus();
                list.setSelectedIndex(0);
                return;
            }
            if (KeyEvent.VK_A <= e.getKeyCode() && e.getKeyCode() <= KeyEvent.VK_Z || e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
                getHint();
        }
    }

    //tạo gợi ý*
    private class ListSelection implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (searchField.isEditable()) {
                searchField.setText(list.getSelectedValue());
                try {
                    search();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    //tạo icon*
    private void createIcon() {
        insButton.setIcon(new ImageIcon("icon/add.png"));
        updButton.setIcon(new ImageIcon("icon/modify.png"));
        delButton.setIcon(new ImageIcon("icon/delete.png"));
        exitButton.setIcon(new ImageIcon("icon/exit.png"));
        speakButton.setIcon(new ImageIcon("icon/speak.png"));
        saveButton.setIcon(new ImageIcon("icon/save.png"));
        logo.setIcon(new ImageIcon("icon/logo.png"));
    }

    public JPanel getMain() {
        return UETDictionary;
    }
}
