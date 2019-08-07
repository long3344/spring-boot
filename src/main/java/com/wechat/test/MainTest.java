package com.wechat.test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MainTest {

    private static List<Staff> staffList = new ArrayList<>();

    public static void main(String[] args) {

        System.out.println("请选指令：1（增加员工），2（查询员工），3（修改员工），4（删除员工）");
        Scanner scanner = new Scanner(System.in);
        switch (scanner.nextInt()){

            case 1:
                System.out.println("================【增加员工】================");
                addStaff();
                System.out.println("员工信息:" + staffList.toString());
                scanner.close();
                break;
            case 2:
                System.out.println("================【查询员工】================");
                queryStaff();

                scanner.close();
                break;
            case 3:
                System.out.println("================【修改员工】================");
                //TODO

                scanner.close();
                break;
            case 4:
                System.out.println("================【删除员工】================");
                //TODO
                scanner.close();
                break;
            case 5:
                System.out.println("================【比较员工】================");
                //TODO
                scanner.close();
                break;
            default:
                System.out.println("指令不存在，请重新输入！");

                scanner.close();
                break;
        }

    }

    /**
     * 查询员工
     */
    private static void queryStaff() {
        System.out.println("请出入员工姓名：");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        staffList.parallelStream().filter(item -> item.getName().equals(name)).collect(Collectors.toList());
    }

    /**
     * 添加员工
     * @return
     */
    public static List<Staff> addStaff(){
        Staff staff = new Staff();
        System.out.println("请输入员工信息，以回车键结束！");
        Scanner scanner = new Scanner(System.in);

        System.out.println("姓名：");
        if (scanner.hasNextLine()){
            staff.setName(scanner.nextLine());
        }
        System.out.print("生日：");
        if (scanner.hasNextLine()){
            staff.setBrithday(scanner.nextLine());
        }
        System.out.print("身份证号：");
        if (scanner.hasNextLine()){
            String id = scanner.nextLine();
            while (id.length() < 10){
                System.out.println("身份证号有误，请重新输入！");
                id = scanner.nextLine();
            }

            staff.setId(id);

            DateFormat format = new SimpleDateFormat("yyyy");
            Integer nowYear = Integer.valueOf(format.format(new Date()));
            Integer birYear = Integer.valueOf(staff.getId().substring(6,10));
            staff.setAge(nowYear - birYear);
        }

        System.out.print("家庭地址：");
        if (scanner.hasNextLine()){
            staff.setHouseAddress(scanner.nextLine());
        }
        System.out.print("手机号：");
        if (scanner.hasNextLine()){
            staff.setPhoneNumber(scanner.nextLine());
        }
        System.out.print("邮箱：");
        if (scanner.hasNextLine()){
            staff.setEmail(scanner.nextLine());
        }
        System.out.println("职务：A（实习生），B（开发主管）,C（项目经理）");
        if (scanner.hasNextLine()){
            staff.setJob(JobEnum.valueOf(scanner.nextLine().toUpperCase()));
        }
        staffList.add(staff);
        scanner.close();
        return staffList;
    }

}
