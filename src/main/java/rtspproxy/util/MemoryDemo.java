package rtspproxy.util;

public class MemoryDemo {
	public static void main(String args[]) {
		Runtime r = Runtime.getRuntime();
		long mem1, mem2;
		Integer someints[] = new Integer[1000];
		System.out.println("Total memory is ：" + r.totalMemory());
		mem1 = r.freeMemory();
		System.out.println("Initial free is : " + mem1);
		r.gc();
		mem1 = r.freeMemory();
		System.out.println("Free memory after garbage collection : " + mem1);
		//allocate integers
		for (int i = 0; i < 1000; i++)
			someints[i] = new Integer(i);

		mem2 = r.freeMemory();
		System.out.println("Free memory after allocation : " + mem2);
		System.out.println("Memory used by allocation : " + (mem1 - mem2));

		//discard Intergers
		for (int i = 0; i < 1000; i++)
			someints[i] = null;
		r.gc(); //request garbage collection
		mem2 = r.freeMemory();
		System.out.println("Free memory after collecting " + "discarded integers : " + mem2);
	}
}
