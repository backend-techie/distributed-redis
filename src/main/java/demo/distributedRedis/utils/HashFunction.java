package demo.distributedRedis.utils;

public class HashFunction {




    public static int getHashValue(final String word) {
        long prime = 61;
        long odd = 59;
        int LIMIT = 4;

        int hash = word.hashCode();
        if (hash < 0) {
            hash = Math.abs(hash);
        }
        return calculateHash(hash, prime, odd, LIMIT);
    }

    private static int calculateHash(final int hash, final long prime, final long odd, final int LIMIT) {
        return (int)((((hash % LIMIT) * prime) % LIMIT) * odd) % LIMIT;
    }


}
