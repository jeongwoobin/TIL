package javaexam.ch7;

public class Dijkstra_Algorithm {
	private int n;
	private int weight[][];
	
	public Dijkstra_Algorithm(int n) {
		this.n = n;
		weight = new int [n+1][n+1];
	}
	
	public void input(int i, int j, int k) {
		weight[i][j] = k;
		weight[j][i] = k;
	}
	
	public void start(int s) {
		int dis[] = new int[n+1];
		boolean[] check = new boolean[n+1];
		
		for(int i = 0; i < n+1; i++) {
			dis[i] = Integer.MAX_VALUE;
		}
		
		dis[s] = 0;
		check[s] = true;
		
		for(int i = 1; i < n+1; i++) {
			if(!check[i] && weight[s][i] != 0) {
				dis[i] = weight[s][i];
			}
		}
		
		for(int l = 0; l < n-1; l++) {
			int min = Integer.MAX_VALUE;
			int min_index = -1;
			
			for(int i = 0; i < n+1; i++) {
				if(!check[i] && dis[i] != Integer.MAX_VALUE) {
					if(dis[i] < min) {
						min = dis[i];
						min_index = i;
					}
				}
			}
			
			check[min_index] = true;
			for(int i = 0; i < n+1; i++) {
				if(!check[i] && weight[min_index][i] != 0) {
					if(dis[i] > dis[min_index] + weight[min_index][i]) {
						dis[i] = dis[min_index] + weight[min_index][i];
					}
				}
			}
		}
		
		System.out.println(s+"번 노드 시작");
		for(int i = 1; i < n+1; i++) {
			System.out.println(i+"번 노드까지 가는 최단거리는 " + dis[i]);
		}
		System.out.println();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Dijkstra_Algorithm a = new Dijkstra_Algorithm(5);	// 노드 5개
		a.input(1, 2, 5);
		a.input(1, 3, 2);
		a.input(2, 5, 2);
		a.input(2, 4, 3);
		a.input(3, 5, 1);
		a.input(3, 4, 5);
		a.input(4, 5, 3);
		
		a.start(1);
		a.start(2);
		a.start(3);
		a.start(4);
		a.start(5);
	}
}



// 참고 : https://bumbums.tistory.com/4