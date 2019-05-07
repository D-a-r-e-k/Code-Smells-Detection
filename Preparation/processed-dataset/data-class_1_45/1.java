public void compare() {
    gc();
    long j = sizeOf(javasoft);
    gc();
    long t = sizeOf(trove);
    long p = Math.round(t * 100 / j * 100) / 100;
    System.out.println("--------------------------");
    System.out.println(description);
    System.out.println("javasoft: " + j);
    System.out.println("trove: " + t);
    System.out.println("trove's collection requires " + p + "% of the memory needed by javasoft's collection");
}
