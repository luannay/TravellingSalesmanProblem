package com.projekty.ania.komiwojazer.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.projekty.ania.komiwojazer.ITSPService;

public class KomiwojazerService extends Service {

    // https://www.sitepoint.com/aidl-for-sharing-functionality-between-android-apps/?fbclid=IwAR0fYrVRmAz6Bp8FilmK-SJaCUV7o1YSX0TynsIs04ovT1MrPfQ_jx4KhQw

    private int[][] graph;
    private boolean[] v; // to check if node has been visited or not

    private final ITSPService.Stub mBinder = new ITSPService.Stub() {
        @Override
        public int getResult() {
            int ans = Integer.MAX_VALUE;
            int n = graph.length; //number of nodes
            // Mark 0th node as visited
            v[0] = true;
            // Find the minimum weight Hamiltonian Cycle
            return tsp(graph, v, 0, n, 1, 0, ans);
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
        graph = new int[][]{{0, 10, 15, 20},
                {10, 0, 35, 25},
                {15, 35, 0, 30},
                {20, 25, 30, 0}};


        v = new boolean[graph.length];
    }

    //https://www.geeksforgeeks.org/travelling-salesman-problem-implementation-using-backtracking/
    // Function to find the minimum weight
    // Hamiltonian Cycle
    private int tsp(int[][] graph, boolean[] v, int currPos, int n, int count, int cost, int ans) {

        // If last node is reached and it has a link
        // to the starting node i.e the source then
        // keep the minimum value out of the total cost
        // of traversal and "ans"
        // Finally return to check for more possible values
        if (count == n && graph[currPos][0] > 0) {
            ans = Math.min(ans, cost + graph[currPos][0]);
            return ans;
        }

        // BACKTRACKING STEP
        // Loop to traverse the adjacency list
        // of currPos node and increasing the count
        // by 1 and cost by graph[currPos,i] value
        for (int i = 0; i < n; i++) {
            if (v[i] == false && graph[currPos][i] > 0) {

                // Mark as visited
                v[i] = true;
                ans = tsp(graph, v, i, n, count + 1,
                        cost + graph[currPos][i], ans);

                // Mark ith node as unvisited
                v[i] = false;
            }
        }
        return ans;
    }

}
