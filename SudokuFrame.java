import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.*;
import javax.swing.text.Document;

import java.awt.*;
import java.awt.event.*;


 public class SudokuFrame extends JFrame {

 	JTextArea puzz,sol;
 	JCheckBox checkBox;
 	JButton checkButton;

	public SudokuFrame() {
		super("Sudoku Solver");
		setLayout(new BorderLayout(4,4));

		puzz = new JTextArea(15,20);
		sol = new JTextArea(15,20);

		puzz.setBorder(new TitledBorder("Puzzle"));
		sol.setBorder(new TitledBorder("Solution"));
		sol.setEditable(false);
		add(puzz, BorderLayout.CENTER);
		add(sol, BorderLayout.EAST);

		JPanel panel = new JPanel(new FlowLayout(0));
		checkBox = new JCheckBox("Auto check:");
		checkBox.setSelected(true);
		checkButton = new JButton("Check");
		panel.add(checkButton);
		panel.add(checkBox);
		add(panel, BorderLayout.SOUTH);

		Document doc = puzz.getDocument();
		doc.addDocumentListener(new PuzzleAreaListener());
		checkButton.addActionListener(new checkButtonListener());

		setLocationByPlatform(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	private class PuzzleAreaListener implements DocumentListener{

		@Override
		public void insertUpdate(DocumentEvent e) {
			if(checkBox.isSelected())
				update();


		}
		@Override
		public void removeUpdate(DocumentEvent e) {
			if(checkBox.isSelected())
				update();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			if(checkBox.isSelected())
				update();
		}

	}
	private class checkButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource().equals(checkButton))
				update();
		}

	}
	 private void update(){
		try{
			String output = "";
			String currText = puzz.getText();
			Sudoku sudo = new Sudoku(currText);
			int cnt = sudo.solve();
			if(cnt > 0){
				output += sudo.getSolutionText();
			}
			output += ("Solutions: " + cnt + "\n");
			output += ("Elapsed :" + sudo.getElapsed() + "ms \n");
			sol.setText(output);

		}catch(Exception e) {
			sol.setText("Invalid input");
		}
	 }

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		SudokuFrame frame = new SudokuFrame();
	}

}
