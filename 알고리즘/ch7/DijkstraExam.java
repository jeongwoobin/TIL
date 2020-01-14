package javaexam.ch7;

import java.util.Arrays;

public class DijkstraExam {
	int n;
	int maps[][];
	
	public DijkstraExam(int n) {
		this.n = n;
		maps = new int[n+1][n+1];
	}
	
	void input(int i, int j, int w) {			// i에서 j로 갈 때 가중치 w
		maps[i][j] = w;
		maps[j][i] = w;
	}
	
	void dijkstra(int v) {
		int distance[] = new int[n+1];
		boolean[] check = new boolean[n+1];
		
		for(int i = 1; i < n+1; i++) {
			distance[i] = Integer.MAX_VALUE;
		}
		
		distance[v] = 0;
		check[v] = true;
		
		for(int i = 1; i < n+1; i++) {
			if(!check[i] && maps[v][i] != 0) {
				distance[i] = maps[v][i];
			}
		}
		
		for(int a = 0; a < n+1; a++) {
			int min = Integer.MAX_VALUE;
			int min_index = -1;
			
			// 최소값 찾기
			for(int i = 1; i < n+1; i++) {
				if(!check[i] && distance[i] != Integer.MAX_VALUE) {
					if(distance[i] < min) {
						min = distance[i];
						min_index = i;
					}
				}
			}
			
			check[min_index] = true;
			for(int i = 1; i < n+1; i++) {
				if(!check[i] && maps[min_index][i] != 0) {
					if(distance[i] > distance[min_index]) {
						distance[i] = distance[min_index] + maps[min_index][i];
					}
				}
			}
			System.out.println(Arrays.toString(distance));
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DijkstraExam app = new DijkstraExam(8);
		
		app.input(1, 2, 3);
		app.input(1, 5, 4);
		app.input(1, 4, 4);
		app.input(2, 3, 2);
		app.input(3, 4, 1);
		app.input(4, 5, 2);
		app.input(5, 6, 4);
		app.input(4, 7, 6);
		app.input(7, 6, 3);
		app.input(3, 8, 3);
		app.input(6, 8, 2);
		
		app.dijkstra(1);
	}

}
