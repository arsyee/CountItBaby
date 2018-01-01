package hu.fallen.countitbaby.helpers;

public class SpiralGrid {
    public static Dim getGridAdjustment(int n, Dim direction) {
        Dim result = getGridAdjustmentUpRight(n);
        int signX = direction.X() >= 0 ? 1 : -1;
        int signY = direction.Y() < 0 ? 1 : -1;
        result.setX(signX * result.X());
        result.setY(signY * result.Y());
        if (Math.abs(direction.X()) > Math.abs(direction.Y())) {
            int temp = result.X();
            result.setX(-1 * signX * signY * result.Y());
            result.setY(-1 * signX * signY * temp);
        }
        return result;
    }

    // https://stackoverflow.com/a/19287522/1409960
    // http://jsfiddle.net/HJQ4g/3/
    private static Dim getGridAdjustmentUpRight(int n) {
        if (n == 0) return new Dim(0, 0);
        // given n an index in the squared spiral
        // p the sum of point in inner square
        // a the position on the current square
        // n = p + a

        int r = (int) (Math.floor((Math.sqrt(n) - 1) / 2) + 1);

        // compute radius : inverse arithmetic sum of 8+16+24+...=
        int p = (8 * r * (r - 1)) / 2;
        // compute total point on radius -1 : arithmetic sum of 8+16+24+...

        int en = r * 2;
        // points by edge

        int a = (n - p) % (r * 8);
        // compute de position and shift it so the first is (-r,-r) but (-r+1,-r)
        // so square can connect

        int[] pos = new int[]{0, 0};
        switch ((int) Math.floor(a / (r * 2))) {
            // find the face : 0 top, 1 right, 2, bottom, 3 left
            case 0:
                pos[0] = a - r;
                pos[1] = -r;
                break;
            case 1:
                pos[0] = r;
                pos[1] = (a % en) - r;
                break;
            case 2:
                pos[0] = r - (a % en);
                pos[1] = r;
                break;
            case 3:
                pos[0] = -r;
                pos[1] = r - (a % en);
                break;
        }
        return new Dim(pos[0], pos[1]);
    }
}
