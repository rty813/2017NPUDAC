package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.AccessibleObject;
import java.util.Scanner;

/*
6
20 18 16 15 14 13
20 0 0 0 0 0
1 14 0 3 1 1
 */
public class Main {
    private static final int N = 10;
    private static int n;
    private static long tail;
    private static int[] queue = new int[N];
    private static int[] queue2 = new int[N];
    private static int step = 0;
    private static int[] target = new int[N];
    private static int[] max = new int[N];
    private static long flag = 1;
    //    private static boolean[] can;
    private static int[] maxValue = new int[10000000];
    private static long maxCacu = 0;
    private static int minStep = -1;

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
            File file = new File("1.txt");
            file.delete();
            file = new File("2.txt");
            file.delete();
            file = new File("result.txt");
            file.delete();

            RandomAccessFile accessFile = new RandomAccessFile("1.txt", "rw");
            RandomAccessFile arrayAccessFile = new RandomAccessFile("2.txt", "rw");

            for (int i = 0; i < 10000000; i++){
                maxValue[i] = 1;
            }

            Scanner in = new Scanner(System.in);
            n = in.nextInt();
            for (int i = 0; i < n; i++) {
                max[i] = in.nextInt();
            }
            for (int i = n-1; i >= 0; i--){
                for (int j = i; j < n; j++){
                    maxValue[i] *= (max[j] + 1);
                }
            }
            for (int i = 0; i < n; i++) {
                queue[i] = in.nextInt();
                accessFile.write(String.format("%d ", queue[i]).getBytes());
            }
            accessFile.write("-1\r\n".getBytes());
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < n; i++) {
                target[i] = in.nextInt();
            }
//            System.exit(0);
            tail = 1;
            accessFile.seek(0);
            int t = 0;
            while (true){
                if (t == flag){
                    flag = tail;
                    step++;
                    System.out.println(String.format("head=%d\tflag=%d\tstep=%d", t, flag, step));
                }
                long prePos = accessFile.getFilePointer();
                if (accessFile.getFilePointer() == accessFile.length()){
                    System.out.println(String.format("Over!\nTotal Status=%d", tail));
                    RandomAccessFile resultAccessFile = new RandomAccessFile("result.txt", "rw");
                    resultAccessFile.seek(resultAccessFile.length());
                    resultAccessFile.write(String.format("Total Status=%d\r\n", tail).getBytes());
                    resultAccessFile.close();
                    break;
                }
                String line = accessFile.readLine();
                String[] lines = line.split(" ");
                for (int i = 0; i <= n; i++){
                    queue[i] = Integer.parseInt(lines[i]);
                }
                arrayAccessFile.seek(cacu(queue));
                arrayAccessFile.writeBoolean(true);
                long pos = accessFile.getFilePointer();

                if ((step > minStep) && (minStep != -1)){
                    System.out.println("Over!");
                    long endTime = System.currentTimeMillis();
                    System.out.println("耗时：" + (endTime - startTime));
                    System.exit(0);
                    break;
                }

                if (check()){
                    if ((step > minStep) && (minStep == -1)){
                        minStep = step;
                    }
                    RandomAccessFile resultAccessFile = new RandomAccessFile("result.txt", "rw");
                    resultAccessFile.write(("Success! step=" + step + "\r\n").getBytes());
                    System.out.println("Success! step=" + step);
                    long temp = prePos;
                    while (temp != -1){
                        System.out.println(line);
                        resultAccessFile.write((line + "\r\n").getBytes());
                        temp = Integer.parseInt(lines[n]);
                        if (temp == -1){
                            break;
                        }
                        accessFile.seek(temp);
                        line = accessFile.readLine();
                        lines = line.split(" ");
                    }
                    resultAccessFile.close();
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
                        arrayAccessFile.seek(cacu(queue2));
                        if (arrayAccessFile.readBoolean()){
                            continue;
                        }
                        arrayAccessFile.seek(cacu(queue2));
                        arrayAccessFile.writeBoolean(true);
                        tail++;
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

            accessFile.close();
            arrayAccessFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int cacu(int[] queue){
        int ans = 0;
        for (int i = n-1; i >= 0; i--){
            ans += maxValue[i+1] * queue[i];
        }
        if (ans > maxCacu){
            maxCacu = ans;
            System.out.println(maxCacu);
        }
        return ans;
    }
}
