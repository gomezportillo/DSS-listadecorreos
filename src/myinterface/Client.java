package myinterface;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;

import model.MyUser;
import myinterface.Client;

public class Client {

	private static final String servlet_URL = "http://localhost:8080/dss-listadecorreos/EmailListServlet";

	private JFrame frmEmailListClient;
	private JTextField nameTF;
	private JTextField surnameTF;
	private JTextField emailTF;
	private JTable table;
	private JLabel lblInfo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					Client window = new Client();
					window.frmEmailListClient.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				try
				{
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				}
				catch(Exception e)
				{
					System.out.println("Error setting native LAF: " + e);
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Client()
	{
		initialize();

		updateTable();
	}

	/**
	 * Initialise the contents of the frame.
	 */
	private void initialize()
	{
		JButton btnAddUser = new JButton("Add user");
		btnAddUser.addActionListener(new ActionListener()
		{
			/**
			 * Pressing "Add user"
			 */
			public void actionPerformed(ActionEvent e)
			{
				String name = nameTF.getText();
				String surname = surnameTF.getText();
				String email = emailTF.getText();

				if (name.equals("") || surname.equals("") || email.equals(""))
				{
					lblInfo.setText("Info: No name, surname nor email can be empty for adding a user.");
					lblInfo.setForeground(Color.orange);
				}
				else
				{
					MyUser user = new MyUser(name, surname, email);

					int response_code = performActionInServer(user, "addUser");

					if (response_code == 0)
					{
						DefaultTableModel model = (DefaultTableModel) table.getModel();
						model.addRow( new Object[]{ name, surname, email } );
						nameTF.setText("");
						surnameTF.setText("");
						emailTF.setText("");

						lblInfo.setText("Info: User with email " + email + " added.");
						lblInfo.setForeground(Color.green);
					}
					else
					{
						lblInfo.setText("Info: Error adding user.");
						lblInfo.setForeground(Color.red);
					}
				}
			}
		});

		JButton btnUpdateUser = new JButton("Update user");
		btnUpdateUser.addActionListener(new ActionListener()
		{
			/**
			 * Pressing "Update user"
			 */
			public void actionPerformed(ActionEvent e)
			{
				String name = nameTF.getText();
				String surname = surnameTF.getText();
				String email = emailTF.getText();

				if (name.equals("") || surname.equals("") || email.equals(""))
				{
					lblInfo.setText("Info: No name, surname nor email can be empty for updating a user.");
					lblInfo.setForeground(Color.orange);
				}
				else
				{
					MyUser user = new MyUser(name, surname, email);

					int response_code = performActionInServer(user, "updateUser");

					if (response_code == 0)
					{
						DefaultTableModel model = (DefaultTableModel) table.getModel();
						model.addRow( new Object[]{ name, surname, email } );
						nameTF.setText("");
						surnameTF.setText("");
						emailTF.setText("");

						updateTable();

						lblInfo.setText("Info: User with email " + email + " updated.");
						lblInfo.setForeground(Color.green);
					}
					else
					{
						lblInfo.setText("Info: Error updating user with email " + email + ".");
						lblInfo.setForeground(Color.red);
					}
				}
			}
		});

		JButton btnDeleteUser = new JButton("Delete user");
		btnDeleteUser.addActionListener(new ActionListener()
		{
			/**
			 * Pressing "Delete user"
			 */
			public void actionPerformed(ActionEvent arg0)
			{

				int rowIndex = table.getSelectedRow();

				if (rowIndex != -1)
				{
					String email = (String) table.getModel().getValueAt(rowIndex, 2);

					Map<String,String> parameters = new HashMap<String, String>();
					parameters.put("action", "deleteUser");
					parameters.put("email", email);
					int response_code = 1;

					try
					{
						ObjectInputStream ois = new ObjectInputStream( performPOST(servlet_URL, parameters) );
						response_code = ois.readInt();
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
						response_code = 1;
					}

					if (response_code == 0)
					{
						((DefaultTableModel) table.getModel()).removeRow( rowIndex );
						lblInfo.setText("Info: User with email " + email + " deleted.");
						lblInfo.setForeground(Color.green);
					}
					else
					{
						lblInfo.setText("Info: Error deleting user with email " + email + ".");
						lblInfo.setForeground(Color.red);
					}
				}
				else
				{
					lblInfo.setText("Info: You must select a row.");
					lblInfo.setForeground(Color.red);
				}
			}
		});

		frmEmailListClient = new JFrame();
		frmEmailListClient.setTitle("Email list client");
		frmEmailListClient.setBounds(100, 100, 876, 516);
		frmEmailListClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 110, 0, 0, 576, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		frmEmailListClient.getContentPane().setLayout(gridBagLayout);

		JLabel lblName = new JLabel("Name");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 1;
		gbc_lblName.gridy = 1;
		frmEmailListClient.getContentPane().add(lblName, gbc_lblName);

		nameTF = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.gridwidth = 2;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 3;
		gbc_textField.gridy = 1;
		frmEmailListClient.getContentPane().add(nameTF, gbc_textField);
		nameTF.setColumns(10);

		JLabel lblEmail = new JLabel("Surname");
		GridBagConstraints gbc_lblEmail = new GridBagConstraints();
		gbc_lblEmail.insets = new Insets(0, 0, 5, 5);
		gbc_lblEmail.gridx = 1;
		gbc_lblEmail.gridy = 3;
		frmEmailListClient.getContentPane().add(lblEmail, gbc_lblEmail);

		surnameTF = new JTextField();
		GridBagConstraints gbc_textField1 = new GridBagConstraints();
		gbc_textField1.gridwidth = 2;
		gbc_textField1.insets = new Insets(0, 0, 5, 5);
		gbc_textField1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField1.gridx = 3;
		gbc_textField1.gridy = 3;
		frmEmailListClient.getContentPane().add(surnameTF, gbc_textField1);
		surnameTF.setColumns(10);

		JLabel lblEmail_1 = new JLabel("Email");
		GridBagConstraints gbc_lblEmail_1 = new GridBagConstraints();
		gbc_lblEmail_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblEmail_1.gridx = 1;
		gbc_lblEmail_1.gridy = 5;
		frmEmailListClient.getContentPane().add(lblEmail_1, gbc_lblEmail_1);

		emailTF = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.gridwidth = 2;
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 3;
		gbc_textField_1.gridy = 5;
		frmEmailListClient.getContentPane().add(emailTF, gbc_textField_1);
		emailTF.setColumns(10);

		GridBagConstraints gbc_btnAddUser = new GridBagConstraints();
		gbc_btnAddUser.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddUser.gridx = 1;
		gbc_btnAddUser.gridy = 7;
		frmEmailListClient.getContentPane().add(btnAddUser, gbc_btnAddUser);

		GridBagConstraints gbc_btnUpdateUser = new GridBagConstraints();
		gbc_btnUpdateUser.insets = new Insets(0, 0, 5, 5);
		gbc_btnUpdateUser.gridx = 3;
		gbc_btnUpdateUser.gridy = 7;
		frmEmailListClient.getContentPane().add(btnUpdateUser, gbc_btnUpdateUser);

		JPanel panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridheight = 2;
		gbc_panel.gridwidth = 4;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 9;
		frmEmailListClient.getContentPane().add(panel, gbc_panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane);

		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		scrollPane.setViewportView(table);
		table.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		table.setModel(new DefaultTableModel(
				new Object[][] { },
				new String[] { "Name", "Surname", "Email" }
				));

		GridBagConstraints gbc_btnDeleteUser = new GridBagConstraints();
		gbc_btnDeleteUser.gridwidth = 2;
		gbc_btnDeleteUser.insets = new Insets(0, 0, 5, 5);
		gbc_btnDeleteUser.gridx = 1;
		gbc_btnDeleteUser.gridy = 12;
		frmEmailListClient.getContentPane().add(btnDeleteUser, gbc_btnDeleteUser);

		lblInfo = new JLabel("Information regarding the system");
		GridBagConstraints gbc_lblInfo = new GridBagConstraints();
		gbc_lblInfo.gridwidth = 2;
		gbc_lblInfo.insets = new Insets(0, 0, 5, 5);
		gbc_lblInfo.gridx = 3;
		gbc_lblInfo.gridy = 12;
		frmEmailListClient.getContentPane().add(lblInfo, gbc_lblInfo);

		JMenuBar menuBar = new JMenuBar();
		frmEmailListClient.setJMenuBar(menuBar);

		JMenu mnMenu = new JMenu("Menu");
		menuBar.add(mnMenu);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				System.exit(0);
			}
		});
		mnMenu.add(mntmExit);
	}

	public InputStream performPOST(String urlString, Map<String,String> map)
	{
		String parameters = "";
		for (Map.Entry<String, String> command : map.entrySet())
		{
			String aux_par = command.getKey() + "=" + command.getValue() + "&";
			parameters += aux_par;
		}

		try
		{
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			OutputStream output = connection.getOutputStream();
			output.write(parameters.getBytes());
			output.flush();
			output.close();
			return connection.getInputStream();
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch(ProtocolException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private List<MyUser> getUserListFromServer()
	{
		List<MyUser> userList = null;
		Map<String,String> parameters = new HashMap<String, String>();
		parameters.put("action", "listUsers");

		try
		{
			ObjectInputStream ois = new ObjectInputStream( performPOST(servlet_URL, parameters) );
			userList = (List<MyUser>) ois.readObject();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return userList;

	}

	private void updateTable()
	{
		DefaultTableModel aux_model;

		// Clean table
		aux_model = (DefaultTableModel) table.getModel();
		aux_model.setRowCount(0);

		List<MyUser> userlist = this.getUserListFromServer();
		for (MyUser u : userlist)
		{
			aux_model = (DefaultTableModel) table.getModel();
			aux_model.addRow( new Object[]{ u.getName(), u.getSurname(), u.getEmail() } );
		}
	}

	private int performActionInServer(MyUser user, String action)
	{
		Map<String,String> parameters = new HashMap<String, String>();
		parameters.put("action", "addUser");
		parameters.put("name", user.getName());
		parameters.put("surname", user.getSurname());
		parameters.put("email", user.getEmail());
		int response_code = 1;

		try
		{
			ObjectInputStream response = new ObjectInputStream(performPOST(servlet_URL, parameters));
			response_code = response.readInt();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			response_code = 1;
		}
		return response_code;
	}
}
