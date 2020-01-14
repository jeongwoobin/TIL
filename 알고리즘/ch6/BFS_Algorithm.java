package javaexam.ch6;

import java.util.Iterator;
import java.util.LinkedList;

public class BFS_Algorithm {
	int nodeNum;
	LinkedList<Integer> list[];
	
	public BFS_Algorithm(int n) {
		nodeNum = n;
		list = new LinkedList[n];
		
		// 노드 리스트 초기화
		for(int i = 0; i < n; i++) {
			list[i] = new LinkedList();
		}
	}
	
	// v노드와 w노드 연결
	void addEdge(int v, int w) {
		list[v].add(w);
	}

	// 파라미터 노드부터 시작
	void BFS(int s) {
		// 노드 방문 여부
		boolean visited[] = new boolean[nodeNum];
		
		// BFS 구현을 위한 큐 생성
		LinkedList<Integer> queue = new LinkedList<>();
		
		visited[s] = true;
		queue.add(s);
		
		while(queue.size() != 0) {		// 큐에 데이터가 있을 때
			s = queue.poll();
			System.out.println(s + " ");
			
			Iterator<Integer> i = list[s].listIterator();
			while(i.hasNext()) {
				int n = i.next();
				if(!visited[n]) {
					visited[n] = true;
					queue.add(n);
				}
			}
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BFS_Algorithm app = new BFS_Algorithm(4);
		
		app.addEdge(0, 1);
		app.addEdge(0, 2);
		app.addEdge(2, 0);
		app.addEdge(2, 3);
		app.addEdge(3, 3);
		
		app.BFS(2);
	}
}
