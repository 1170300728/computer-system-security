#!/usr/bin/python
# -*- coding: UTF-8 -*-
from tkinter import *
import tkinter.messagebox
import pymysql
import time

class login():
    def __init__(self,window):
        self.window=window

        self.login_frame = Frame(self.window)
        Label(self.login_frame, text="请输入用户姓名：").pack(side=TOP, padx=5, pady=10)
        self.user_entry = Entry(self.login_frame, width=50)
        self.user_entry.pack(side=TOP)

        Label(self.login_frame, text="请输入用户密码：").pack(side=TOP, padx=5, pady=10)
        self.pwd_entry = Entry(self.login_frame, show="*", width=50)
        self.pwd_entry.pack(side=TOP)
        self.login_frame.pack()

        self.b=Button(self.login_frame, text="登录", command=self.switch_manager)
        self.b.pack(side=BOTTOM)

    def switch_manager(self,):
        username=self.user_entry.get()
        pwd=self.pwd_entry.get()
        self.login_frame.destroy()
        manager(self.window,username,pwd)


class manager():
    def __init__(self,window,username,pwd):
        self.username=username
        self.pwd=pwd
        self.window=window

        self.manager_frame=Frame(self.window)
        self.manager_frame.pack()
        if(self.username=='ttn' and self.pwd=='ttn123'):
            self.conn=pymysql.connect(
                host='localhost',
                port=3306, 
                user=self.username,
                password=self.pwd,
                db='zhongmai_science',
                charset='utf8'
            )
            self.journal=open('journal.txt','w')
            
            print (1)
            Label(self.manager_frame, text="职员1的姓名：").pack(side=TOP, padx=5, pady=10)
            self.user1_entry = Entry(self.manager_frame, width=50)
            self.user1_entry.pack(side=TOP)
            Label(self.manager_frame, text="职员1的密码：").pack(side=TOP, padx=5, pady=10)
            self.pwd1_entry = Entry(self.manager_frame, width=50)
            self.pwd1_entry.pack(side=TOP)
            Label(self.manager_frame, text="职员2的姓名：").pack(side=TOP, padx=5, pady=10)
            self.user2_entry = Entry(self.manager_frame, width=50)
            self.user2_entry.pack(side=TOP)
            Label(self.manager_frame, text="职员3的姓名：").pack(side=TOP, padx=5, pady=10)
            self.user3_entry = Entry(self.manager_frame, width=50)
            self.user3_entry.pack(side=TOP)
            Label(self.manager_frame, text="对什么的权限：").pack(side=TOP, padx=5, pady=10)
            self.cap_entry = Entry(self.manager_frame, width=50)
            self.cap_entry.pack(side=TOP)
            Label(self.manager_frame, text="对那个表的权限：").pack(side=TOP, padx=5, pady=10)
            self.table_entry = Entry(self.manager_frame, width=50)
            self.table_entry.pack(side=TOP)

            self.displaytxt=StringVar()
            self.txt = Label(self.manager_frame,textvariable=self.displaytxt)
            self.txt.pack(side=BOTTOM)
            
            self.b1 = Button(self.manager_frame, text="使公司职员获取权限", command=self.grant_cap)
            self.b1.pack(side=BOTTOM)
            self.b2 = Button(self.manager_frame, text="使公司职员失去权限", command=self.revoke_cap)
            self.b2.pack(side=BOTTOM)
            self.b3 = Button(self.manager_frame, text="添加职员", command=self.New_User)
            self.b3.pack(side=BOTTOM)
            self.b4 = Button(self.manager_frame, text="退出", command=self.out)
            self.b4.pack(side=BOTTOM)
        else:
            tkinter.messagebox.askokcancel('登录失败','你没有中脉科技有限公司的管理者身份！')
            self.manager_frame.destroy
            login(self.window)

    
    def grant_cap(self,):
        whom=[]
        user1=self.user1_entry.get()
        user2=self.user2_entry.get()
        user3=self.user3_entry.get()
        cap=self.cap_entry.get()
        table=self.table_entry.get()
        if(user1!=""):
            whom.append(user1)
        if(user2!=""):
            whom.append(user2)
        if(user3!=""):
            whom.append(user3)
        if(cap=="" or table==""):
            return
        for w in whom:
            cursor=self.conn.cursor()
            sql='grant '+ cap+' on zhongmai_science.'+table+' to '+w+"@'%'"+';'
            print(sql)
            cursor.execute(sql)
            localtime = time.asctime( time.localtime(time.time()) )
            self.journal.write( sql+"        "+localtime+"\n")
            self.conn.commit()
            cursor.close()
        self.displaytxt.set("提升权限成功")
        

    def revoke_cap(self,):
        whom=[]
        user1=self.user1_entry.get()
        user2=self.user2_entry.get()
        user3=self.user3_entry.get()
        cap=self.cap_entry.get()
        table=self.table_entry.get()
        if(user1!=""):
            whom.append(user1)
        if(user2!=""):
            whom.append(user2)
        if(user3!=""):
            whom.append(user3)
        if(cap=="" or table==""):
            return
        print(whom)
        for w in whom:
            cursor=self.conn.cursor()
            sql='revoke '+ cap+' on zhongmai_science.'+table+' from '+w+"@'%'"+';'
            print(sql)
            cursor.execute(sql)
            localtime = time.asctime( time.localtime(time.time()) )
            self.journal.write( sql+"        "+localtime+"\n")
            self.conn.commit()
            cursor.close()
        self.displaytxt.set("降低权限成功")
        
    def New_User(self,):
        user1=self.user1_entry.get()
        pwd=self.pwd1_entry.get()
        if(user1!=""and pwd!=""):
            cursor=self.conn.cursor()
            sql="create user "+user1+" identified by'"+pwd+"'"+';'
            print(sql)
            cursor.execute(sql)
            localtime = time.asctime( time.localtime(time.time()) )
            self.journal.write( sql+"        "+localtime+"\n")
            self.conn.commit()
            cursor.close()
        self.displaytxt.set("新建成功")
            
    def out(self,):
        self.conn.close()
        self.journal.close()
        self.manager_frame.destroy()
        self.window.destroy()

# class employee_ctrl():

if __name__ == '__main__':
    window= Tk()
    window.title("中脉公司管理系统")

    login(window)

    window.mainloop()


'''
while True:
    
    user=input('user>>: ').strip()
    pwd=input('password>>: ').strip()

    # 建立链接
    conn=pymysql.connect(
        host='localhost',
        port=3306, 
        user=user,
        password=pwd,
        db='zhongmai_science',
        charset='utf8'
    )

    journal=open('journal.txt','w')
    while True:
        choice=input('choice: ')
        if choice=='1':
            cursor=conn.cursor()
            what=input("select what:")
            sql="select * from "+what+";" 
            cursor.execute(sql)
            localtime = time.asctime( time.localtime(time.time()) )
            journal.write( sql+"        "+localtime+"\n")
            res=cursor.fetchall()
            print(res) 
            cursor.close()
        
        if choice=='2':
            cursor=conn.cursor()
            what=input("insert what:")
            values=input("insert values:")
            sql='insert into '+what+' values('+values+');' 
            print(sql)
            cursor.execute(sql)
            localtime = time.asctime( time.localtime(time.time()) )
            journal.write( sql+"        "+localtime+"\n")
            conn.commit()
            cursor.close()

        if choice=='3':
            cursor=conn.cursor()
            what=input("delete what:")
            where=input("delete where:")
            wherevalue=input("delete wherevalue:")
            sql='delete from '+what+' where '+where+'='+wherevalue+';' 
            print(sql)
            cursor.execute(sql)
            localtime = time.asctime( time.localtime(time.time()) )
            journal.write( sql+"        "+localtime+"\n")
            conn.commit()
            cursor.close()

        if choice=='4':
            cursor=conn.cursor()
            what=input("update what:")
            setwhat=input("update set what:")
            setvalue=input("update set value:")
            where=input("direct where:")
            wherevalue=input("direct wherevalue:")
            sql='update '+what+' set '+setwhat+'='+setvalue+' where '+where+'='+wherevalue+';'
            print(sql)
            cursor.execute(sql)
            localtime = time.asctime( time.localtime(time.time()) )
            journal.write( sql+"        "+localtime+"\n")
            conn.commit()
            cursor.close()

        if choice=='5'and user=='ttn':
            cursor=conn.cursor()
            userName=input("input user's name:")
            password=input("user's password:")
            sql="create user "+userName+" identified by'"+password+"'"+';'
            print(sql)
            cursor.execute(sql)
            localtime = time.asctime( time.localtime(time.time()) )
            journal.write( sql+"        "+localtime+"\n")
            conn.commit()
            cursor.close()

        if choice=='6' and user=='ttn':
            cursor=conn.cursor()
            capability=input("grant what capability:")
            tabel=input("which tabel:")
            whom=input("to whom:")
            sql='grant '+ capability+' on '+tabel+' to '+whom+'@ttn'+';'
            print(sql)
            cursor.execute(sql)
            localtime = time.asctime( time.localtime(time.time()) )
            journal.write( sql+"        "+localtime+"\n")
            conn.commit()
            cursor.close()
            
        if choice=='7' and user=='ttn':
            capability=input("grant what capability:")
            tabel=input("which tabel:")
            whom=[]
            w=input("to whom:")
            while w != "no":
                whom.append(w)
                w=input("someone else?:")
            for w in whom:
                cursor=conn.cursor()
                sql='grant '+ capability+' on '+tabel+' to '+w+'@ttn'+';'
                print(sql)
                cursor.execute(sql)
                localtime = time.asctime( time.localtime(time.time()) )
                journal.write( sql+"        "+localtime+"\n")
                conn.commit()
                cursor.close()

        if choice=='8' and user=='ttn':
            cursor=conn.cursor()
            capability=input("revoke what capability:")
            tabel=input("which tabel:")
            whom=input("to whom:")
            sql='revoke '+ capability+' on '+tabel+' from '+whom+'@ttn'+';'
            print(sql)
            cursor.execute(sql)
            localtime = time.asctime( time.localtime(time.time()) )
            journal.write( sql+"        "+localtime+"\n")
            conn.commit()
            cursor.close()

        if choice=='9' and user=='ttn':
            capability=input("revoke what capability:")
            tabel=input("which tabel:")
            whom=[]
            w=input("to whom:")
            while w != "no":
                whom.append(w)
                w=input("someone else?:")
            for w in whom:
                cursor=conn.cursor()
                sql='revoke '+ capability+' on '+tabel+' from '+w+'@ttn'+';'
                print(sql)
                cursor.execute(sql)
                localtime = time.asctime( time.localtime(time.time()) )
                journal.write( sql+"        "+localtime+"\n")
                conn.commit()
                cursor.close()
        if choice=="q":
            break
    journal.close()
    
    conn.close()
    out=input('logout or not>>: ').strip()
    if out=="out":
        break
'''

