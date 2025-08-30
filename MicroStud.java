import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.*;
import java.sql.*;
import javax.swing.table.*;

class NewRecordFrame extends JInternalFrame implements ActionListener, InternalFrameListener
{
    private JLabel lbl_title, lbl_rollno, lbl_sname, lbl_dept, lbl_dob, lbl_result;
    private JTextField txt_rollno, txt_sname, txt_dob;
    private JComboBox <String> jcbox_dept;
    private JButton btn_insert, btn_clear;
    private Vector <String> dept;

    public NewRecordFrame()
    {
        super("New Record", true, true, true, true);

        this.setLayout(null);

        lbl_title = new JLabel("*** Add New Student ***", JLabel.CENTER);
        lbl_title.setBounds(50, 10, 270, 50);
        lbl_title.setFont(new Font("Gabriola", Font.BOLD, 30));
        lbl_title.setForeground(Color.RED);
        this.add(lbl_title);

        lbl_rollno = new JLabel("Roll Number");
        lbl_rollno.setBounds(10, 70, 150, 35);
        this.add(lbl_rollno);

        txt_rollno = new JTextField();
        txt_rollno.setBounds(170, 70, 150, 35);
        this.add(txt_rollno);

        lbl_sname = new JLabel("Student Name");
        lbl_sname.setBounds(10, 115, 150, 35);
        this.add(lbl_sname);

        txt_sname = new JTextField();
        txt_sname.setBounds(170, 115, 150, 35);
        this.add(txt_sname);

        lbl_dept = new JLabel("Department");
        lbl_dept.setBounds(10, 160, 150, 35);
        this.add(lbl_dept);

        dept = new Vector<String>(3);
        String arr[] = {"Select Deptartment", "CSE", "IT"};
        for(String d: arr)
        dept.add(d);

        jcbox_dept = new JComboBox<String>(dept);
        jcbox_dept.setBounds(170, 160, 150, 35);
        this.add(jcbox_dept);

        lbl_dob = new JLabel("D.O.B. (YYYY-MM-DD)");
        lbl_dob.setBounds(10, 205, 150, 35);
        this.add(lbl_dob);

        txt_dob = new JTextField();
        txt_dob.setBounds(170, 205, 150, 35);
        this.add(txt_dob);

        btn_insert = new JButton("INSERT");
        btn_insert.setBounds(10, 250, 150, 35);
        this.add(btn_insert);
        btn_insert.addActionListener(this);

        btn_clear = new JButton("CLEAR");
        btn_clear.setBounds(170, 250, 150, 35);
        this.add(btn_clear);
        btn_clear.addActionListener(this);

        lbl_result = new JLabel("*****", JLabel.CENTER);
        lbl_result.setBounds(10, 295, 380, 35);
        lbl_result.setForeground(Color.BLUE);
        lbl_result.setFont(new Font("Calibri", Font.BOLD, 15));
        this.add(lbl_result);

        this.addInternalFrameListener(this);        
        this.setSize(400, 400);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        String cmd_text = e.getActionCommand();

        switch (cmd_text) 
        {
            case "INSERT":
                try
                {
                    String rn, sname="", dept="", dob="", err_msg = "";
                    int rollno = 0;

                    rn = txt_rollno.getText().trim();
                    sname = txt_sname.getText().trim().toUpperCase();
                    dob = txt_dob.getText().trim();

                    if(rn.length() == 0)
                    err_msg = err_msg + "Roll No. is required\n";
                    else
                    rollno = Integer.parseInt(rn);

                    if(sname.length() == 0)
                    err_msg = err_msg + "Student name is required\n";

                    if(jcbox_dept.getSelectedIndex() == 0)
                    err_msg = err_msg + "Department is required\n";
                    else
                    dept = jcbox_dept.getSelectedItem().toString();

                    if(dob.length() == 0)
                    err_msg = err_msg +"D.O.B. is required\n";

                    if(err_msg.length() == 0)
                    {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mydb", "root", "9373307767");                                                                     
                        String sql = "insert into mystudents values (?, ?, ?, ?)";
                        PreparedStatement ps = con.prepareStatement(sql);
                        ps.setInt(1, rollno);                    
                        ps.setString(2, sname);
                        ps.setString(3, dept);
                        ps.setDate(4, java.sql.Date.valueOf(dob));

                        int n = ps.executeUpdate();
                        ps.close();
                        con.close();

                        if(n == 1)
                        {
                            lbl_result.setForeground(Color.BLUE);
                            lbl_result.setText("Success! Record inserted..");
                        }
                        else
                        {
                            lbl_result.setForeground(Color.RED);
                            lbl_result.setText("Oops! Record not inserted..");
                        }                        
                    }
                    else
                    JOptionPane.showMessageDialog(this, err_msg);
                }
                catch(Exception ex)
                {
                    JOptionPane.showMessageDialog(this, ex);
                }
                break;
        
            case "CLEAR":
                txt_rollno.setText("");
                txt_sname.setText("");
                jcbox_dept.setSelectedIndex(0);
                txt_dob.setText("");                
                lbl_result.setText("");
                break;
        }
    }

    @Override
    public void internalFrameOpened(InternalFrameEvent e){}

    @Override
    public void internalFrameActivated(InternalFrameEvent e){}

    @Override
    public void internalFrameDeactivated(InternalFrameEvent e){}

    @Override
    public void internalFrameIconified(InternalFrameEvent e){}

    @Override
    public void internalFrameDeiconified(InternalFrameEvent e){}

    @Override
    public void internalFrameClosed(InternalFrameEvent e){}
    @Override
    public void internalFrameClosing(InternalFrameEvent e)
    {
        MyFrame.f1 = null;
    }
}
//
//
//
//

class SearchUpdateDeleteFrame extends JInternalFrame implements ActionListener, InternalFrameListener
{
    private JSplitPane split_pane;
    private JPanel top_panel, bottom_panel;
    
    // top_panel
    private JLabel lbl_rn;
    private JTextField txt_rn;
    private JButton btn_search, btn_clear;    

    // bottom_panel
    private JLabel lbl_title, lbl_rollno, lbl_sname, lbl_dept, lbl_dob, lbl_result;
    private JTextField txt_rollno, txt_sname, txt_dob;
    private JComboBox <String> jcbox_dept;
    private Vector <String> dept;
    private JButton btn_update, btn_delete;

    // database related objects
    private static Connection con;
    private static PreparedStatement ps;

    public SearchUpdateDeleteFrame()
    {
        super("SEARCH | UPDATE | DELETE", true, true, true, true);

        // top_panel
        top_panel = new JPanel();
        top_panel.setLayout(null);

        lbl_rn = new JLabel("Roll Number");
        lbl_rn.setBounds(10, 10, 150, 35);
        top_panel.add(lbl_rn);

        txt_rn = new JTextField();
        txt_rn.setBounds(170, 10, 150, 35);
        top_panel.add(txt_rn);

        btn_search = new JButton("SEARCH");
        btn_search.setBounds(330, 10, 100, 35);
        top_panel.add(btn_search);
        btn_search.addActionListener(this);

        btn_clear = new JButton("CLEAR");
        btn_clear.setBounds(440, 10, 100, 35);
        top_panel.add(btn_clear);
        btn_clear.addActionListener(this);

        // bottom_panel
        bottom_panel = new JPanel();
        bottom_panel.setLayout(null);

        lbl_title = new JLabel("** SEARCH | UPDATE | DELETE **", JLabel.CENTER);
        lbl_title.setBounds(50, 10, 400, 50);
        lbl_title.setFont(new Font("Gabriola", Font.BOLD, 30));
        lbl_title.setForeground(Color.RED);
        bottom_panel.add(lbl_title);

        lbl_rollno = new JLabel("Roll Number");
        lbl_rollno.setBounds(10, 70, 150, 35);
        bottom_panel.add(lbl_rollno);

        txt_rollno = new JTextField();
        txt_rollno.setBounds(170, 70, 150, 35);
        bottom_panel.add(txt_rollno);
        txt_rollno.setEditable(false);        

        lbl_sname = new JLabel("Student Name");
        lbl_sname.setBounds(10, 115, 150, 35);
        bottom_panel.add(lbl_sname);

        txt_sname = new JTextField();
        txt_sname.setBounds(170, 115, 150, 35);
        bottom_panel.add(txt_sname);

        lbl_dept = new JLabel("Department");
        lbl_dept.setBounds(10, 160, 150, 35);
        bottom_panel.add(lbl_dept);

        String arr[] = {"Select Department", "CSE", "IT"};
        dept = new Vector<String>();

        for(String d : arr)
        dept.add(d);

        jcbox_dept = new JComboBox<String>(dept);
        jcbox_dept.setBounds(170, 160, 150, 35);
        bottom_panel.add(jcbox_dept);

        lbl_dob = new JLabel("D.O.B. (YYYY-MM-DD)");
        lbl_dob.setBounds(10, 205, 150, 35);
        bottom_panel.add(lbl_dob);

        txt_dob = new JTextField();
        txt_dob.setBounds(170, 205, 150, 35);
        bottom_panel.add(txt_dob);

        btn_update = new JButton("UPDATE");
        btn_update.setBounds(10, 250, 150, 35);
        bottom_panel.add(btn_update);
        btn_update.addActionListener(this);

        btn_delete = new JButton("DELETE");
        btn_delete.setBounds(170, 250, 150, 35);
        bottom_panel.add(btn_delete);
        btn_delete.addActionListener(this);

        lbl_result = new JLabel("*****", JLabel.CENTER);
        lbl_result.setBounds(10, 295, 430, 50);
        bottom_panel.add(lbl_result);
        lbl_result.setForeground(Color.BLUE);

        // split_pane
        split_pane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, top_panel, bottom_panel);
        this.add(split_pane, BorderLayout.CENTER);
        split_pane.setDividerLocation(60);
        split_pane.setDividerSize(1);

        this.addInternalFrameListener(this);
        this.setSize(570, 450);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        String cmd_text = e.getActionCommand();
        String err_msg = "", rno = "";

        try
        {
            switch (cmd_text) 
            {
                case "SEARCH":
                    lbl_result.setText("");
                    err_msg = "";

                    rno = txt_rn.getText().trim();

                    if(rno.length() == 0)
                    err_msg = err_msg + "Enter roll-no. to search\n";
                    else
                    {
                        int roll = Integer.parseInt(rno);

                        String sql = "select * from mystudents where roll_no = ?";
                        connect();
                        ps = con.prepareStatement(sql);
                        ps.setInt(1, roll);

                        ResultSet rs = ps.executeQuery();
                        if(rs.next())
                        {
                            int r = rs.getInt("roll_no");
                            String sn = rs.getString("stud_name");
                            String d = rs.getString("dept");
                            java.sql.Date birth_date = rs.getDate("dob");   
                            
                            txt_rollno.setText(r+"");
                            txt_sname.setText(sn);
                            jcbox_dept.setSelectedItem(d);
                            txt_dob.setText(birth_date.toString());
                        }
                        else
                        {
                            txt_rollno.setText("");
                            txt_sname.setText("");
                            jcbox_dept.setSelectedIndex(0);
                            txt_dob.setText("");
                            JOptionPane.showMessageDialog(this, "Roll No. not exists");
                        }

                        rs.close();
                        ps.close();
                        disconnect();
                    }
                    break;
                
                case "CLEAR":                    
                    txt_rn.setText("");
                    txt_rollno.setText("");
                    txt_sname.setText("");
                    jcbox_dept.setSelectedIndex(0);
                    txt_dob.setText("");
                    lbl_result.setText("");
                    break;

                case "UPDATE":  
                    err_msg = "";
                    rno = txt_rollno.getText().trim();
                    String sn = txt_sname.getText().trim().toUpperCase();
                    String d = jcbox_dept.getSelectedItem().toString();
                    String birth_date = txt_dob.getText().trim();
                    
                    int roll_no = 0;

                    if(rno.length() == 0)
                    err_msg = err_msg + "Roll No. is required\n";
                    else
                    roll_no = Integer.parseInt(rno);

                    if(sn.length() == 0)
                    err_msg = err_msg + "Student Name is required\n";

                    if(jcbox_dept.getSelectedIndex() == 0)
                    err_msg = err_msg + "Department is required\n";
                    else
                    d = jcbox_dept.getSelectedItem().toString();

                    if(birth_date.length() == 0)
                    err_msg = err_msg + "D.O.B. is required\n";

                    if(err_msg.length() == 0)
                    {
                        String sql = "update mystudents set stud_name = ?, dept = ?, dob = ? where roll_no = ?";                                                        
                        connect();
                        ps = con.prepareStatement(sql);
                        ps.setString(1, sn);
                        ps.setString(2, d);
                        ps.setDate(3, java.sql.Date.valueOf(birth_date));
                        ps.setInt(4, roll_no);

                        int n = ps.executeUpdate();
                        ps.close();
                        disconnect();

                        if(n == 1)
                        {
                            lbl_result.setForeground(Color.BLUE);
                            lbl_result.setText("Success! Changes saved. Record Updated..");
                        }
                        else
                        {
                            lbl_result.setForeground(Color.RED);
                            lbl_result.setText("Oops! Changes not saved.");
                        }
                    }
                    else
                    JOptionPane.showMessageDialog(this, err_msg);                    
                    break;
        
                case "DELETE":
                    lbl_result.setText("");
                    err_msg = "";
                    rno = txt_rollno.getText().trim();
                    if(rno.length() == 0)
                    err_msg = err_msg + "Record not selected to delete";
                    else
                    {
                        int dialog_result = JOptionPane.showConfirmDialog(this, "Are you sure?", "Delete", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);                         
                        
                        if(dialog_result == JOptionPane.YES_OPTION)
                        {
                            int rn = Integer.parseInt(rno);
                            String sql = "delete from mystudents where roll_no = ?";
                            connect();
                            ps = con.prepareStatement(sql);
                            ps.setInt(1, rn);
                            int n = ps.executeUpdate();
                            ps.close();
                            disconnect();

                            if(n == 1)
                            {
                                JOptionPane.showMessageDialog(this, "Record deleted..");
                                                               
                                lbl_result.setForeground(Color.BLUE);
                                lbl_result.setText("Record deleted..");

                                //txt_rn.setText(""); 
                                txt_rollno.setText("");
                                txt_sname.setText("");
                                jcbox_dept.setSelectedIndex(0);                                
                                txt_dob.setText("");                                
                            }
                            else
                            {
                                lbl_result.setForeground(Color.RED);
                                lbl_result.setText("Oops! Record not deleted..");
                            }
                        }
                    }
                    break;
            }
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(this, ex);
        }
    }

    public static void connect() throws SQLException, ClassNotFoundException
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con =  DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mydb", "root", "9373307767");
    }

    public static void disconnect() throws SQLException
    {
        if(con.isClosed() == false)
        con.close();
    }

    @Override
    public void internalFrameOpened(InternalFrameEvent e){}

    @Override
    public void internalFrameActivated(InternalFrameEvent e){}

    @Override
    public void internalFrameDeactivated(InternalFrameEvent e){}

    @Override
    public void internalFrameIconified(InternalFrameEvent e){}

    @Override
    public void internalFrameDeiconified(InternalFrameEvent e){}

    @Override
    public void internalFrameClosed(InternalFrameEvent e){}
    @Override
    public void internalFrameClosing(InternalFrameEvent e)
    {
        MyFrame.f2 = null;
    }
}
//
//
//
//
//
class AllStudentsFrame extends JInternalFrame implements InternalFrameListener
{
    private JLabel lbl_title;
    private JScrollPane scroll_pane;
    private JTable students_table;
    private DefaultTableModel table_model;
    private Vector <String> columns;
    private Vector <Vector> rows;
    private Vector <String> single_row;

    public AllStudentsFrame()
    {
        super("ALL STUDENTS", true, true, true, true);

        lbl_title = new JLabel("*** ALL STUDENTS ***", JLabel.CENTER);
        lbl_title.setFont(new Font("Gabriola", Font.BOLD, 30));
        lbl_title.setForeground(Color.RED);
        this.add(lbl_title, BorderLayout.NORTH);

        columns = new Vector<String>(4);
        String arr[] = {"ROLL NUMBER", "STDUENT NAME", "DEPARTMENT", "DATE OF BIRTH"};

        for(String cn : arr)
        columns.add(cn);

        rows = new Vector<Vector>();

        table_model = new DefaultTableModel(rows, columns);
        students_table = new JTable(table_model);
        scroll_pane = new JScrollPane(students_table);
        this.add(scroll_pane, BorderLayout.CENTER);

        JTableHeader th = students_table.getTableHeader();
        th.setBackground(Color.PINK);
        th.setForeground(Color.BLUE);
        th.setFont(new Font("Calibri", Font.BOLD, 15));

        getStudentRecords();

        this.addInternalFrameListener(this);
        this.setSize(500, 300);
        this.setVisible(true);
    }

    public void getStudentRecords()
    {
        try
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/mydb", "root", "9373307767");
            
            String sql = "select * from mystudents";
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next())
            {
                int rn = rs.getInt("roll_no");
                String sn = rs.getString("stud_name");
                String d = rs.getString("dept");
                java.sql.Date birth_date = rs.getDate("dob");
                
                single_row = new Vector<String>(4);
                single_row.add(rn+"");
                single_row.add(sn);
                single_row.add(d);
                single_row.add(birth_date.toString());

                rows.add(single_row);
            }

            rs.close();
            ps.close();
            con.close();

            table_model.setDataVector(rows, columns);
            students_table.setModel(table_model);
        }
        catch(Exception ex)
        {
            JOptionPane.showMessageDialog(this, ex);            
        }
    }

    @Override
    public void internalFrameOpened(InternalFrameEvent e){}

    @Override
    public void internalFrameActivated(InternalFrameEvent e){}

    @Override
    public void internalFrameDeactivated(InternalFrameEvent e){}

    @Override
    public void internalFrameIconified(InternalFrameEvent e){}

    @Override
    public void internalFrameDeiconified(InternalFrameEvent e){}

    @Override
    public void internalFrameClosed(InternalFrameEvent e){}
    @Override
    public void internalFrameClosing(InternalFrameEvent e)
    {
        MyFrame.f3 = null;
    }
}

class MyFrame extends JFrame implements ActionListener
{
    private JToolBar tool_bar;
    private JDesktopPane desktop_pane;
    public static NewRecordFrame f1;
    public static SearchUpdateDeleteFrame f2;
    public static AllStudentsFrame f3;

    public MyFrame()
    {
        tool_bar = new JToolBar();
        this.add(tool_bar, BorderLayout.NORTH);
        tool_bar.setFloatable(false);
        tool_bar.setLayout(new GridLayout(1, 3));

        String arr[] = {"ADD NEW STUDENT", "SEARCH | UPDATE | DELETE", "ALL STUDENTS"};

        for(int i = 0; i < arr.length; i++)
        {
            JButton b = new JButton(arr[i]);
            tool_bar.add(b);
            b.addActionListener(this);
            b.setFont(new Font("Gabriola", Font.BOLD, 30));
            b.setBackground(Color.MAGENTA);
            b.setForeground(Color.WHITE);
        }

        desktop_pane = new JDesktopPane();
        this.add(desktop_pane, BorderLayout.CENTER);

        this.setSize(1200, 600);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        String cmd_text = e.getActionCommand();

        switch(cmd_text)
        {
            case "ADD NEW STUDENT":
                if(f1 == null)
                {
                    f1 = new NewRecordFrame();
                    f1.setBounds(10, 10, 400, 400);
                    desktop_pane.add(f1);
                }
                break;
            
            case "SEARCH | UPDATE | DELETE":
                if(f2 == null)
                {
                    f2 = new SearchUpdateDeleteFrame();
                    f2.setBounds(420, 10, 570, 450);
                    desktop_pane.add(f2);
                }
                break;
            
            case "ALL STUDENTS":
                if(f3 == null)
                {
                    f3 = new AllStudentsFrame();
                    f3.setBounds(50, 50, 500, 300);
                    desktop_pane.add(f3);
                }
                break;                                
        }
    }
}

class MicroStud
{
    public static void main(String[] args) 
    {
        MyFrame f = new MyFrame();
    }
}







