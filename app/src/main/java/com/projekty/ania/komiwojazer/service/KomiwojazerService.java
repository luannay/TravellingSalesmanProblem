package com.projekty.ania.komiwojazer.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

import com.projekty.ania.komiwojazer.aidlService;

public class KomiwojazerService extends Service {

    private int[][] graph;
    private boolean[] nodeVisited;

    //https://www.sitepoint.com/aidl-for-sharing-functionality-between-android-apps/?fbclid=IwAR0fYrVRmAz6Bp8FilmK-SJaCUV7o1YSX0TynsIs04ovT1MrPfQ_jx4KhQw


    private final aidlService.Stub mBinder = new aidlService.Stub() {

        @Override
        public int getProduct() {
            int ans = Integer.MAX_VALUE;
            int nodesNumber = graph.length;
            nodeVisited[0] = true;

            return hc(graph, nodeVisited, 0, nodesNumber, 1, 0, ans);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        graph = new int[][]{
                {0, 4, 1, 9},
                {3, 0, 6, 11},
                {4, 1, 0, 2},
                {6, 5, -4, 0}};

        nodeVisited = new boolean[graph.length];
    }

    //https://www.geeksforgeeks.org/travelling-salesman-problem-implementation-using-backtracking/

    private int hc(int[][] graph, boolean[] v, int currentPosition, int n, int count, int cost, int result) {

        // If last node is reached and it has a link
        // to the starting node i.e the source then
        // keep the minimum value out of the total cost
        // of traversal and "ans"
        // Finally return to check for more possible values
        if (count == n && graph[currentPosition][0] > 0) {
            result = Math.min(result, cost + graph[currentPosition][0]);
            return result;
        }

        // BACKTRACKING STEP
        // Loop to traverse the adjacency list
        // of currPos node and increasing the count
        // by 1 and cost by graph[currPos,i] value
        for (int i = 0; i < n; i++) {
            if (v[i] == false && graph[currentPosition][i] > 0) {

                // Mark as visited
                v[i] = true;
                result = hc(graph, v, i, n, count + 1,
                        cost + graph[currentPosition][i], result);

                // Mark ith node as unvisited
                v[i] = false;
            }
        }
        return result;
    }

}
