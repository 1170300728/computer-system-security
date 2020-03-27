package bank.system;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import bank.util.Administrator;
import bank.util.ChiefAdministrator;
import bank.util.Todo;
import bank.util.Client;
import bank.util.Information;
import bank.util.Log;

public class enter {
  public static long current_client_id;
  public static String current_user_name;
  public static long current_manager_id;
  public static String current_manager_name;
  public static long deposit;
  public static Todo todo;


  public static void main(String[] args) {
    // 查询数据库，存储相应的信息
    MysqlConnect.getClientInformation();
    MysqlConnect.getManagerInformation();
    MysqlConnect.getChiefManagerInformation();
    MysqlConnect.getToDoListInformation();
    // 初始化 账单信息
    if (Information.Todos != null) {
      if (Information.Todos.size() == 0) {
        todo = new Todo();
      } else {
        todo = Information.Todos.get(0);
      }
    }
    // 进入登录界面
    loginPage();
    new Log();
  }

  /**
   * 用户登录
   */
  private static void loginPage() {
    JFrame frame = new JFrame("银行系统设计——用户登录");
    frame.setSize(400, 300);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // 创建面板
    JPanel panel = new JPanel();
    panel.setLayout(null); // 面板布局

    // 创建 标签 & 输入框 & 按钮
    JLabel userLabel = new JLabel("输入用户:");
    JLabel passwordLabel = new JLabel("输入密码:");
    JLabel identityLabel = new JLabel("输入身份:");
    JTextField userNameText = new JTextField(25);
    JTextField userPasswordText = new JTextField(25);
    JTextField identityText = new JTextField(25);
    JButton loginButton = new JButton("登录");
    JButton registerButton = new JButton("注册");

    // 设置标签的大小和位置
    userLabel.setBounds(40, 20, 80, 25);
    userNameText.setBounds(100, 20, 165, 25);
    passwordLabel.setBounds(40, 50, 80, 25);
    userPasswordText.setBounds(100, 50, 165, 25);
    identityLabel.setBounds(40, 80, 80, 25);
    identityText.setBounds(100, 80, 165, 25);
    loginButton.setBounds(40, 110, 80, 25);
    registerButton.setBounds(40, 140, 80, 25);

    // 设置面板内容
    panel.add(userLabel);
    panel.add(userNameText);
    panel.add(passwordLabel);
    panel.add(userPasswordText);
    panel.add(identityLabel);
    panel.add(identityText);
    panel.add(loginButton);
    panel.add(registerButton);

    // 将面板加入到窗口中
    frame.add(panel);

    // 按钮的监听事件
    loginButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        // 检测身份
        int flag = 0;
        if (identityText.getText().equals("用户")) {
          // 查找该用户
          for (Client client : Information.clients) {
            if (client.getUserName().equals(userNameText.getText())
                && client.getUserPassword().equals(userPasswordText.getText())) {
              enter.deposit = client.getDeposit();
              enter.current_client_id = client.getId();
              enter.current_user_name = client.getUserName();
              try {
                Date now = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String current_time = dateFormat.format(now);
                Log.output
                    .write((current_time + "\t 用户 " + current_user_name + " 登录成功\n").getBytes());
              } catch (IOException e2) {
                System.out.println("写入日志失败!!!");
              }
              clientService();
              frame.dispose();
              flag = 1;
            }
          }
        } else if (identityText.getText().equals("管理员")) {
          // 查找该用户
          for (Administrator administrator : Information.administrators) {
            if (administrator.getUserName().equals(userNameText.getText())
                && administrator.getUserPassword().equals(userPasswordText.getText())) {
              enter.current_manager_id = administrator.getId();
              enter.current_manager_name = administrator.getUserName();
              managerService();
              frame.dispose();
              flag = 1;
              // 写日志
              try {
                Date now = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String current_time = dateFormat.format(now);
                Log.output.write(
                    (current_time + "\t 管理员 " + current_manager_name + " 登录成功\n").getBytes());
              } catch (IOException e2) {
                System.out.println("写入日志失败!!!");
              }
            }
          }
        }else if (identityText.getText().equals("上级管理员")) {
          // 查找该用户
          for (ChiefAdministrator chiefadministrator : Information.chiefadministrators) {
            if (chiefadministrator.getUserName().equals(userNameText.getText())
                && chiefadministrator.getUserPassword().equals(userPasswordText.getText())) {
              enter.current_manager_id = chiefadministrator.getId();
              enter.current_manager_name = chiefadministrator.getUserName();
              chiefManagerService();
              frame.dispose();
              flag = 1;
              // 写日志
              try {
                Date now = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String current_time = dateFormat.format(now);
                Log.output.write(
                    (current_time + "\t 上级管理员 " + current_manager_name + " 登录成功\n").getBytes());
              } catch (IOException e2) {
                System.out.println("写入日志失败!!!");
              }
            }
          }
        }
        // 没有查找到用户信息
        if (flag == 0) {
          frame.dispose();
          loginFailedPage();
        }
      }
    });

    // 注册按钮的监听事件
    registerButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        frame.dispose();
        registerPage();
      }
    });

    // 设置窗口可见
    frame.setVisible(true);
  }

  // 注册界面
  private static void registerPage() {
    JFrame frame = new JFrame("银行系统设计——用户注册");
    frame.setSize(600, 400);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // 创建面板
    JPanel panel = new JPanel();
    panel.setLayout(null); // 面板布局

    // 创建 标签 & 输入框 & 按钮
    JLabel userLabel = new JLabel("用户:");
    JLabel passwordLabel = new JLabel("密码:");
    JLabel identityLabel = new JLabel("注册身份:");
    JTextField userNameText = new JTextField(20);
    JTextField userPasswordText = new JTextField(20);
    JTextField identityText = new JTextField(20);
    JButton registerButton = new JButton("注册");
    JButton returnButton = new JButton("返回");

    // 设置标签的大小和位置
    userLabel.setBounds(10, 20, 80, 25);
    userNameText.setBounds(100, 20, 165, 25);
    passwordLabel.setBounds(10, 50, 80, 25);
    userPasswordText.setBounds(100, 50, 165, 25);
    identityLabel.setBounds(10, 80, 80, 25);
    identityText.setBounds(100, 80, 165, 25);
    registerButton.setBounds(10, 110, 80, 25);
    returnButton.setBounds(10, 140, 80, 25);

    // 设置面板内容
    panel.add(userLabel);
    panel.add(userNameText);
    panel.add(passwordLabel);
    panel.add(userPasswordText);
    panel.add(identityLabel);
    panel.add(identityText);
    panel.add(registerButton);
    panel.add(returnButton);

    registerButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        // 插入数据库用户信息
        if (identityText.getText().equals("用户")) {
          Client client = new Client();
          client.setUserName(userNameText.getText());
          client.setUserPassword(userPasswordText.getText());
          client.setDeposit(0);
          MysqlConnect.insertClient(client);
          // 写日志
          try {
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String current_time = dateFormat.format(now);
            Log.output
                .write((current_time + "\t 增加用户 " + userNameText.getText() + " 成功 \n").getBytes());
          } catch (IOException e2) {
            System.out.println("写入日志失败!!!");
          }
        } else if (identityText.getText().equals("管理员")) {
          Administrator administrator = new Administrator();
          administrator.setUserName(userNameText.getText());
          administrator.setUserPassword(userPasswordText.getText());
          MysqlConnect.insertManager(administrator);
          // 写日志
          try {
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String current_time = dateFormat.format(now);
            Log.output
                .write((current_time + "\t 增加管理员 " + userNameText.getText() + " 成功 \n").getBytes());
          } catch (IOException e2) {
            System.out.println("写入日志失败!!!");
          }
        }else if (identityText.getText().equals("上级管理员")) {
        ChiefAdministrator chiefadministrator = new ChiefAdministrator();
        chiefadministrator.setUserName(userNameText.getText());
        chiefadministrator.setUserPassword(userPasswordText.getText());
        MysqlConnect.insertChiefManager(chiefadministrator);
        // 写日志
        try {
          Date now = new Date();
          SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
          String current_time = dateFormat.format(now);
          Log.output
              .write((current_time + "\t 增加上级管理员 " + userNameText.getText() + " 成功 \n").getBytes());
        } catch (IOException e2) {
          System.out.println("写入日志失败!!!");
        }
      }
        frame.dispose();
    }
    });

    returnButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        frame.dispose();
        loginPage();
      }
    });

    // 将面板加入窗口中
    frame.add(panel);
    // 设置窗口可见
    frame.setVisible(true);
  }

  // 登录失败界面
  private static void loginFailedPage() {
    JFrame frame = new JFrame("用户登录失败");
    frame.setSize(300, 200);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // 创建面板
    JPanel panel = new JPanel();
    panel.setLayout(null); // 面板布局

    // 创建 标签 & 按钮
    JLabel messageLabel = new JLabel("不存在该用户或密码错误");
    JButton loginAgainButton = new JButton("重新登录");

    // 设置标签和按钮的大小和位置
    messageLabel.setBounds(60, 40, 165, 30);
    loginAgainButton.setBounds(60, 80, 150, 30);

    // 加入面板中
    panel.add(messageLabel);
    panel.add(loginAgainButton);

    loginAgainButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        // 跳到登录界面
        loginPage();
        frame.dispose();
      }
    });

    // 将面板加入窗口中
    frame.add(panel);
    // 设置窗口可见
    frame.setVisible(true);
  }

  // 用户服务界面
  private static void clientService() {
    JFrame frame = new JFrame("客户服务");
    frame.setSize(600, 400);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // 创建面板
    JPanel panel = new JPanel();
    panel.setLayout(null); // 面板布局

    // 创建 选项按钮
    JButton depositButton = new JButton("存款");
    JButton withdrawButton = new JButton("取款");
    JButton queryButton = new JButton("查询余额");
    JButton returnButton = new JButton("注销登录");

    // 设置标签和按钮的大小和位置
    depositButton.setBounds(60, 80, 165, 30);
    withdrawButton.setBounds(60, 120, 165, 30);
    queryButton.setBounds(60, 160, 165, 30);
    returnButton.setBounds(60, 200, 165, 30);

    // 加入面板中
    panel.add(depositButton);
    panel.add(withdrawButton);
    panel.add(queryButton);
    panel.add(returnButton);

    // 设置 存款 按钮监听事件
    depositButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        // 关掉当前界面
        frame.dispose();
        // 跳转到存款页面
        depositPage();
      }
    });

    // 设置 取款 按钮监听事件
    withdrawButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        // 关掉当前界面
        frame.dispose();
        // 跳转到取款页面
        withdrawPage();
      }
    });

    // 设置 查询 按钮监听事件
    queryButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        // 关掉当前界面
        frame.dispose();
        // 跳转到查询界面
        queryPage();
      }
    });

    // 设置 返回 按钮监听事件
    returnButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        // 关掉当前界面
        frame.dispose();
        // 跳转到登录界面
        loginPage();
      }
    });

    // 将面板加入窗口中
    frame.add(panel);
    // 设置窗口可见
    frame.setVisible(true);
  }

  // 存款页面
  private static void depositPage() {
    JFrame frame = new JFrame("存款页面");
    frame.setSize(600, 400);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // 创建面板
    JPanel panel = new JPanel();
    panel.setLayout(null); // 面板布局

    // 创建 标签 & 输入框 & 按钮
    JLabel depositRemindLabel = new JLabel("存款金额：");
    JTextField moneyNumberText = new JTextField(30);
    JButton ensureButton = new JButton("确定");
    JButton returnButton = new JButton("返回");

    // 设置标签和按钮的大小和位置
    depositRemindLabel.setBounds(60, 40, 75, 30);
    moneyNumberText.setBounds(135, 40, 75, 30);
    ensureButton.setBounds(60, 80, 150, 30);
    returnButton.setBounds(60, 120, 150, 30);

    // 加入面板中
    panel.add(depositRemindLabel);
    panel.add(moneyNumberText);
    panel.add(ensureButton);
    panel.add(returnButton);

    ensureButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Todo todo_temp = new Todo();
        todo_temp.setUser_id(current_client_id);
        todo_temp.setName(current_user_name);
        todo_temp.setMoney(Integer.parseInt(moneyNumberText.getText()));
        todo_temp.setStatue(0);
        // 跳转到管理员
        int id = MysqlConnect.insertToDoList(todo_temp);
        todo = todo_temp;
        todo_temp.setId(id);
        MysqlConnect.getToDoListInformation();
        loginPage();
        // 写日志
        try {
          Date now = new Date();
          SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
          String current_time = dateFormat.format(now);
          Log.output.write((current_time + "\t 用户 " + current_user_name + " 提出了 "
              + moneyNumberText.getText() + " 元的存款请求\n").getBytes());
        } catch (IOException e2) {
          System.out.println("写入日志失败!!!");
        }
      }
    });

    returnButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        // 跳转到客户服务界面
        frame.dispose();
        clientService();
      }
    });

    // 将面板加入窗口中
    frame.add(panel);
    // 设置窗口可见
    frame.setVisible(true);
  }

  // 取款页面
  private static void withdrawPage() {
    JFrame frame = new JFrame("取款页面");
    frame.setSize(600, 400);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // 创建面板
    JPanel panel = new JPanel();
    panel.setLayout(null); // 面板布局

    // 创建 标签 & 输入框 & 按钮
    JLabel withdrawRemindLabel = new JLabel("取款金额：");
    JTextField moneyNumberText = new JTextField(30);
    JButton ensureButton = new JButton("确定");
    JButton returnButton = new JButton("返回");

    // 设置标签和按钮的大小和位置
    withdrawRemindLabel.setBounds(60, 40, 75, 30);
    moneyNumberText.setBounds(135, 40, 75, 30);
    ensureButton.setBounds(60, 80, 150, 30);
    returnButton.setBounds(60, 120, 150, 30);

    // 加入面板中
    panel.add(withdrawRemindLabel);
    panel.add(moneyNumberText);
    panel.add(ensureButton);
    panel.add(returnButton);

    ensureButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Todo todo_temp = new Todo();
        todo_temp.setUser_id(current_client_id);
        todo_temp.setName(current_user_name);
        todo_temp.setMoney(-Integer.parseInt(moneyNumberText.getText()));
        todo_temp.setStatue(0);
        int id = MysqlConnect.insertToDoList(todo_temp);
        todo = todo_temp;
        todo_temp.setId(id);
        // 跳转到管理员
        MysqlConnect.getToDoListInformation();
        loginPage();
        // 写日志
        try {
          Date now = new Date();
          SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
          String current_time = dateFormat.format(now);
          Log.output.write((current_time + "\t 用户 " + current_user_name + " 提出了 "
              + moneyNumberText.getText() + " 元的取款请求\n").getBytes());
        } catch (IOException e2) {
          System.out.println("写入日志失败!!!");
        }
      }
    });

    returnButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        // 跳转到客户服务界面
        frame.dispose();
        clientService();
      }
    });

    // 将面板加入窗口中
    frame.add(panel);
    // 设置窗口可见
    frame.setVisible(true);
  }

  // 查询页面
  private static void queryPage() {
    JFrame frame = new JFrame("查询页面");
    frame.setSize(600, 400);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // 创建面板
    JPanel panel = new JPanel();
    panel.setLayout(null); // 面板布局

    // 创建 标签 & 输入框 & 按钮
    JLabel withdrawRemindLabel = new JLabel("余额：" + deposit);
    JLabel waitingEnsuredLabel = new JLabel("待确认金额：" + todo.getMoney());
    JButton ensureButton = new JButton("确定");
    JButton returnButton = new JButton("返回");

    // 设置标签和按钮的大小和位置
    withdrawRemindLabel.setBounds(60, 40, 165, 30);
    waitingEnsuredLabel.setBounds(60, 80, 165, 30);
    ensureButton.setBounds(60, 120, 150, 30);
    returnButton.setBounds(60, 160, 150, 30);

    // 加入面板中
    panel.add(withdrawRemindLabel);
    panel.add(waitingEnsuredLabel);
    panel.add(ensureButton);
    panel.add(returnButton);

    ensureButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // 关闭查询页面
        frame.dispose();
      }
    });

    returnButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        frame.dispose();
        clientService();
      }
    });

    // 将面板加入窗口中
    frame.add(panel);
    // 设置窗口可见
    frame.setVisible(true);
  }

  // 管理员服务界面
  private static void managerService() {
    JFrame frame = new JFrame("管理员服务");
    frame.setSize(600, 400);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // 创建面板
    JPanel panel = new JPanel();
    panel.setLayout(null); // 面板布局

    // 创建 选项按钮
    JLabel todoMessage;
    if (todo.getMoney() < 0) {
      todoMessage =
          new JLabel("账单：" + "用户 " + todo.getName() + " 取出 " + Math.abs(todo.getMoney()) + " 金额");
    } else {
      todoMessage = new JLabel("账单：" + "用户 " + todo.getName() + " 存储 " + todo.getMoney() + " 金额");
    }
    JButton agreeButton = new JButton("同意");
    if (todo.getMoney() < -10000) {
      agreeButton.setText("同意并提交给上级");
    } else {
      agreeButton.setText("同意");
    }
    JButton disagreeButton = new JButton("不同意");

    // 设置标签和按钮的大小和位置
    todoMessage.setBounds(30, 80, 300, 30);
    agreeButton.setBounds(30, 120, 150, 30);
    disagreeButton.setBounds(30, 160, 155, 30);

    // 加入面板中
    panel.add(todoMessage);
    panel.add(agreeButton);
    panel.add(disagreeButton);

    // 同意的话，删除账单，并更新存款
    agreeButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        // 删除账单或更改订单状态
        if (todo.getMoney() >= -10000) {
          MysqlConnect.deleteToDo(String.valueOf(todo.getId()));
        } else {
          todo.setStatue(1);
          MysqlConnect.updateToDo(String.valueOf(todo.getId()));
        }
        // 查找用户存款
        for (Client cli : Information.clients) {
          if (cli.getUserName().equals(todo.getName())) {
            deposit = cli.getDeposit();
          }
        }
        if (todo.getMoney() < 0) {
          // 写日志
          try {
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String current_time = dateFormat.format(now);
            Log.output.write((current_time + "\t 管理员 " + current_user_name + " 同意了用户 "
                + current_user_name + " 的 " + Math.abs(todo.getMoney()) + " 元的取款请求\n").getBytes());
          } catch (IOException e2) {
            System.out.println("写入日志失败!!!");
          }
        } else {
          // 写日志
          try {
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String current_time = dateFormat.format(now);
            Log.output.write((current_time + "\t 管理员 " + current_user_name + " 同意了用户 "
                + current_user_name + " 的 " + todo.getMoney() + " 元的存款请求\n").getBytes());
          } catch (IOException e2) {
            System.out.println("写入日志失败!!!");
          }
        }
        if (deposit + todo.getMoney() < 0) {
          // 写日志
          try {
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String current_time = dateFormat.format(now);
            Log.output
                .write((current_time + "\t 余额不足\t 用户 " + current_user_name + " 取款失败\n").getBytes());
          } catch (IOException e2) {
            System.out.println("写入日志失败!!!");
          }
        } else {
          if (todo.getMoney() < 0) {
            // 写日志
            try {
              Date now = new Date();
              SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
              String current_time = dateFormat.format(now);
              Log.output.write((current_time + "\t 用户 " + current_user_name + " 取款 "
                  + Math.abs(todo.getMoney()) + " 成功\n").getBytes());
            } catch (IOException e2) {
              System.out.println("写入日志失败!!!");
            }
          } else {
            // 写日志
            try {
              Date now = new Date();
              SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
              String current_time = dateFormat.format(now);
              Log.output.write(
                  (current_time + "\t 用户 " + current_user_name + " 存款 " + todo.getMoney() + " 成功\n")
                      .getBytes());
            } catch (IOException e2) {
              System.out.println("写入日志失败!!!");
            }
          }
          if (todo.getMoney() >= -10000) {
            // 更新存款
            MysqlConnect.updateDeposit(todo.getUser_id(), deposit + todo.getMoney());
            // 更新数据库用户信息
            MysqlConnect.getClientInformation();
          }
        }
        for (Client cli : Information.clients) {
          if (cli.getUserName().equals(enter.current_user_name)) {
            deposit = cli.getDeposit();
          }
        }
        MysqlConnect.getToDoListInformation();
        if (Information.Todos.size() == 0) {
          todo = new Todo();
        } else {
          todo = Information.Todos.get(0);
        }
        frame.dispose();
      }
    });

    // 不同意的话，删除账单。
    disagreeButton.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        if (todo.getMoney() < 0) {
          // 写日志
          try {
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String current_time = dateFormat.format(now);
            Log.output.write((current_time + "\t 管理员 " + current_user_name + " 拒绝了用户 "
                + current_user_name + " 的 " + Math.abs(todo.getMoney()) + " 元的取款请求\n").getBytes());
          } catch (IOException e2) {
            System.out.println("写入日志失败!!!");
          }
        } else {
          // 写日志
          try {
            Date now = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String current_time = dateFormat.format(now);
            Log.output.write((current_time + "\t 管理员 " + current_user_name + " 拒绝了用户 "
                + current_user_name + " 的 " + todo.getMoney() + " 元的存款请求\n").getBytes());
          } catch (IOException e2) {
            System.out.println("写入日志失败!!!");
          }
        }
        // 删除账单
        MysqlConnect.deleteToDo(String.valueOf(todo.getId()));
        MysqlConnect.getToDoListInformation();
        if (Information.Todos.size() == 0) {
          todo = new Todo();
        } else {
          todo = Information.Todos.get(0);
        }
        frame.dispose();
      }
    });

    // 将面板加入窗口中
    frame.add(panel);
    // 设置窗口可见
    frame.setVisible(true);
  }



  // 上级管理员服务界面
  private static void chiefManagerService() {
    JFrame frame = new JFrame("上级管理员服务");
    frame.setSize(600, 400);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    if (todo.getStatue() >0) {
      // 创建面板
      JPanel panel = new JPanel();
      panel.setLayout(null); // 面板布局

      // 创建 选项按钮
      JLabel todoMessage;
      if (todo.getMoney() < 0) {
        todoMessage =
            new JLabel("账单：" + "用户 " + todo.getName() + " 取出 " + Math.abs(todo.getMoney()) + " 金额");
      } else {
        todoMessage = new JLabel("账单：" + "用户 " + todo.getName() + " 存储 " + todo.getMoney() + " 金额");
      }
      JButton agreeButton = new JButton("同意");
      JButton disagreeButton = new JButton("不同意");

      // 设置标签和按钮的大小和位置
      todoMessage.setBounds(30, 80, 300, 30);
      agreeButton.setBounds(30, 120, 150, 30);
      disagreeButton.setBounds(30, 160, 155, 30);

      // 加入面板中
      panel.add(todoMessage);
      panel.add(agreeButton);
      panel.add(disagreeButton);

      // 同意的话，删除账单，并更新存款
      agreeButton.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
          // 删除账单
          MysqlConnect.deleteToDo(String.valueOf(todo.getId()));
          // 查找用户存款
          for (Client cli : Information.clients) {
            if (cli.getUserName().equals(todo.getName())) {
              deposit = cli.getDeposit();
            }
          }
          if (todo.getMoney() < 0) {
            // 写日志
            try {
              Date now = new Date();
              SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
              String current_time = dateFormat.format(now);
              Log.output.write(
                  (current_time + "\t 上级管理员 " + current_user_name + " 同意了用户 " + current_user_name
                      + " 的 " + Math.abs(todo.getMoney()) + " 元的取款请求\n").getBytes());
            } catch (IOException e2) {
              System.out.println("写入日志失败!!!");
            }
          } else {
            // 写日志
            try {
              Date now = new Date();
              SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
              String current_time = dateFormat.format(now);
              Log.output.write((current_time + "\t 上级管理员 " + current_user_name + " 同意了用户 "
                  + current_user_name + " 的 " + todo.getMoney() + " 元的存款请求\n").getBytes());
            } catch (IOException e2) {
              System.out.println("写入日志失败!!!");
            }
          }
          if (deposit + todo.getMoney() < 0) {
            // 写日志
            try {
              Date now = new Date();
              SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
              String current_time = dateFormat.format(now);
              Log.output.write(
                  (current_time + "\t 余额不足\t 用户 " + current_user_name + " 取款失败\n").getBytes());
            } catch (IOException e2) {
              System.out.println("写入日志失败!!!");
            }
          } else {
            if (todo.getMoney() < 0) {
              // 写日志
              try {
                Date now = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String current_time = dateFormat.format(now);
                Log.output.write((current_time + "\t 用户 " + current_user_name + " 取款 "
                    + Math.abs(todo.getMoney()) + " 成功\n").getBytes());
              } catch (IOException e2) {
                System.out.println("写入日志失败!!!");
              }
            } else {
              // 写日志
              try {
                Date now = new Date();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                String current_time = dateFormat.format(now);
                Log.output.write((current_time + "\t 用户 " + current_user_name + " 存款 "
                    + todo.getMoney() + " 成功\n").getBytes());
              } catch (IOException e2) {
                System.out.println("写入日志失败!!!");
              }
            }
            // 更新存款
            MysqlConnect.updateDeposit(todo.getUser_id(), deposit + todo.getMoney());
            // 更新数据库用户信息
            MysqlConnect.getClientInformation();
          }
          for (Client cli : Information.clients) {
            if (cli.getUserName().equals(enter.current_user_name)) {
              deposit = cli.getDeposit();
            }
          }
          MysqlConnect.getToDoListInformation();
          if (Information.Todos.size() == 0) {
            todo = new Todo();
          } else {
            todo = Information.Todos.get(0);
          }
          frame.dispose();
        }
      });

      // 不同意的话，删除账单。
      disagreeButton.addActionListener(new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
          if (todo.getMoney() < 0) {
            // 写日志
            try {
              Date now = new Date();
              SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
              String current_time = dateFormat.format(now);
              Log.output.write(
                  (current_time + "\t 上级管理员 " + current_user_name + " 拒绝了用户 " + current_user_name
                      + " 的 " + Math.abs(todo.getMoney()) + " 元的取款请求\n").getBytes());
            } catch (IOException e2) {
              System.out.println("写入日志失败!!!");
            }
          } else {
            // 写日志
            try {
              Date now = new Date();
              SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
              String current_time = dateFormat.format(now);
              Log.output.write((current_time + "\t 上级管理员 " + current_user_name + " 拒绝了用户 "
                  + current_user_name + " 的 " + todo.getMoney() + " 元的存款请求\n").getBytes());
            } catch (IOException e2) {
              System.out.println("写入日志失败!!!");
            }
          }
          // 删除账单
          MysqlConnect.deleteToDo(String.valueOf(todo.getId()));
          MysqlConnect.getToDoListInformation();
          if (Information.Todos.size() == 0) {
            todo = new Todo();
          } else {
            todo = Information.Todos.get(0);
          }
          frame.dispose();
        }
      });

      // 将面板加入窗口中
      frame.add(panel);
      // 设置窗口可见
      frame.setVisible(true);
    }
  }
}

