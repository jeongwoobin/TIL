package javaexam.ch6;

import java.util.Iterator;
import java.util.LinkedList;

public class BFS_Algorithm {
	int nodeNum;
	LinkedList<Integer> list[];
	
	public BFS_Algorithm(int n) {
		nodeNum = n;
		list = new LinkedList[n];
		
		// ��� ����Ʈ �ʱ�ȭ
		for(int i = 0; i < n; i++) {
			list[i] = new LinkedList();
		}
	}
	
	// v���� w��� ����
	void addEdge(int v, int w) {
		list[v].add(w);
	}

	// �Ķ���� ������ ����
	void BFS(int s) {
		// ��� �湮 ����
		boolean visited[] = new boolean[nodeNum];
		
		// BFS ������ ���� ť ����
		LinkedList<Integer> queue = new LinkedList<>();
		
		visited[s] = true;
		queue.add(s);
		
		while(queue.size() != 0) {		// ť�� �����Ͱ� ���� ��
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
