package com.company;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.AccessibleObject;
import java.util.Scanner;


public class Main {
    private static final int N = 5;
    private static int n;
    private static long tail;
    private static int[] queue = new int[N];
    private static int[] queue2 = new int[N];
    private static int step = 0;
    private static int[] target = new int[N];
    private static int[] max = new int[N];
    private static long flag = 1;
    private static int minStep = 0;

    private static boolean check(){
        for (int i = 0; i < n; i++){
            if (queue[i] != target[i]){
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        try {
            RandomAccessFile accessFile = new RandomAccessFile("1.txt", "rw");
            Scanner in = new Scanner(System.in);
            n = in.nextInt();
            for (int i = 0; i < n; i++) {
                max[i] = in.nextInt();
            }
            for (int i = 0; i < n; i++) {
                queue[i] = in.nextInt();
                accessFile.write(String.format("%d ", queue[i]).getBytes());
            }
            accessFile.write("-1\r\n".getBytes());
            for (int i = 0; i < n; i++) {
                target[i] = in.nextInt();
            }
//            System.exit(0);
            tail = 1;
            long startTime = System.currentTimeMillis();
            accessFile.seek(0);
            int t = 0;
            while (true){
                if (t == flag){
                    step++;
                    flag = tail;
                }
                long prePos = accessFile.getFilePointer();
                String line = accessFile.readLine();
                String[] lines = line.split(" ");
                for (int i = 0; i <= n; i++){
                    queue[i] = Integer.parseInt(lines[i]);
                }
                long pos = accessFile.getFilePointer();

                if ((step > minStep) && (minStep != 0)){
                    System.out.println("over!");
                    long endTime = System.currentTimeMillis();
                    System.out.println("程序运行时间：" + (endTime - startTime) + "ms");
                    System.exit(0);
                }

                if (check()){
                    if ((step > minStep) && (minStep == 0)){
                        minStep = step;
                    }
                    System.out.println("Success! step=" + step);
                    long temp = prePos;
                    while (temp != -1){
                        System.out.println(line);
                        temp = Integer.parseInt(lines[n]);
                        if (temp == -1){
                            break;
                        }
                        accessFile.seek(temp);
                        line = accessFile.readLine();
                        lines = line.split(" ");
                    }
//                    new Scanner(System.in).nextLine();
                }

                for (int i = 0; i < n; i++){
                    if (queue[i] == 0){
                        continue;
                    }
                    for (int j = 0; j < n; j++){
                        if ((j == i) || (queue[j] == max[j])){
                            continue;
                        }
                        for (int k = 0; k < n; k++){
                            queue2[k] = queue[k];
                        }
                        if (queue[j] + queue[i] > max[j]){
                            queue2[j] = max[j];
                            queue2[i] = queue[i] - (max[j] - queue[j]);
                        }
                        else{
                            queue2[j] = queue[j] + queue[i];
                            queue2[i] = 0;
                        }
                        tail++;
                        if (tail % 1000000 == 0){
                            System.out.println(String.format("head=%d\ttail=%d\tstep=%d\tflag=%d", t, tail, step, flag));
                        }
                        accessFile.seek(accessFile.length());
                        for (int k = 0; k < n; k++){
                            accessFile.write(String.format("%d ", queue2[k]).getBytes());
                        }
                        accessFile.write(String.format("%d\r\n", prePos).getBytes());

//                    System.out.println(tail);
                    }
                }
                accessFile.seek(pos);
                t++;
            }

//            accessFile.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        System.out.println("hello world");

    }
}
