package hu.fallen.countitbaby.hu.fallen.countitbaby.helpers;

import org.junit.Test;
import static org.junit.Assert.*;

import hu.fallen.countitbaby.helpers.Dim;
import hu.fallen.countitbaby.helpers.SpiralGrid;

public class Model_Canvas_Spiral_UnitTest {

    @Test
    public void test_UpRight_Spiral() {
        Dim direction = new Dim(1, -2);
        assertEquals(SpiralGrid.getGridAdjustment(0, direction), new Dim(0, 0));
        assertEquals(SpiralGrid.getGridAdjustment(1, direction), new Dim(0, -1));
        assertEquals(SpiralGrid.getGridAdjustment(2, direction), new Dim(1, -1));
        assertEquals(SpiralGrid.getGridAdjustment(3, direction), new Dim(1, 0));
        assertEquals(SpiralGrid.getGridAdjustment(4, direction), new Dim(1, 1));
        assertEquals(SpiralGrid.getGridAdjustment(5, direction), new Dim(0, 1));
        assertEquals(SpiralGrid.getGridAdjustment(6, direction), new Dim(-1, 1));
        assertEquals(SpiralGrid.getGridAdjustment(7, direction), new Dim(-1, 0));
        assertEquals(SpiralGrid.getGridAdjustment(8, direction), new Dim(-1, -1));

        assertEquals(SpiralGrid.getGridAdjustment(15, direction), new Dim(2, 1));
        assertEquals(SpiralGrid.getGridAdjustment(25, direction), new Dim(-2, -3));
        assertEquals(SpiralGrid.getGridAdjustment(35, direction), new Dim(3, 2));
        assertEquals(SpiralGrid.getGridAdjustment(45, direction), new Dim(-3, 0));
    }

    @Test
    public void test_RightUp_Spiral() {
        Dim direction = new Dim(2, -1);
        assertEquals(SpiralGrid.getGridAdjustment(1, direction), new Dim(1, 0));
        assertEquals(SpiralGrid.getGridAdjustment(3, direction), new Dim(0, -1));
        assertEquals(SpiralGrid.getGridAdjustment(5, direction), new Dim(-1, 0));
        assertEquals(SpiralGrid.getGridAdjustment(7, direction), new Dim(0, 1));
    }

    @Test
    public void test_UpLeft_Spiral() {
        Dim direction = new Dim(-1, -2);
        assertEquals(SpiralGrid.getGridAdjustment(1, direction), new Dim(0, -1));
        assertEquals(SpiralGrid.getGridAdjustment(3, direction), new Dim(-1, 0));
        assertEquals(SpiralGrid.getGridAdjustment(5, direction), new Dim(0, 1));
        assertEquals(SpiralGrid.getGridAdjustment(7, direction), new Dim(1, 0));
    }

    @Test
    public void test_LeftUp_Spiral() {
        Dim direction = new Dim(-2, -1);
        assertEquals(SpiralGrid.getGridAdjustment(1, direction), new Dim(-1, 0));
        assertEquals(SpiralGrid.getGridAdjustment(3, direction), new Dim(0, -1));
        assertEquals(SpiralGrid.getGridAdjustment(5, direction), new Dim(1, 0));
        assertEquals(SpiralGrid.getGridAdjustment(7, direction), new Dim(0, 1));
    }

    @Test
    public void test_DownRight_Spiral() {
        Dim direction = new Dim(1, 2);
        assertEquals(SpiralGrid.getGridAdjustment(0, direction), new Dim(0, 0));
        assertEquals(SpiralGrid.getGridAdjustment(1, direction), new Dim(0, 1));
        assertEquals(SpiralGrid.getGridAdjustment(2, direction), new Dim(1, 1));
        assertEquals(SpiralGrid.getGridAdjustment(3, direction), new Dim(1, 0));
        assertEquals(SpiralGrid.getGridAdjustment(4, direction), new Dim(1, -1));
        assertEquals(SpiralGrid.getGridAdjustment(5, direction), new Dim(0, -1));
        assertEquals(SpiralGrid.getGridAdjustment(6, direction), new Dim(-1, -1));
        assertEquals(SpiralGrid.getGridAdjustment(7, direction), new Dim(-1, 0));
        assertEquals(SpiralGrid.getGridAdjustment(8, direction), new Dim(-1, 1));

        assertEquals(SpiralGrid.getGridAdjustment(15, direction), new Dim(2, -1));
        assertEquals(SpiralGrid.getGridAdjustment(25, direction), new Dim(-2, 3));
        assertEquals(SpiralGrid.getGridAdjustment(35, direction), new Dim(3, -2));
        assertEquals(SpiralGrid.getGridAdjustment(45, direction), new Dim(-3, 0));
    }

    @Test
    public void test_RightDown_Spiral() {
        Dim direction = new Dim(2, 1);
        assertEquals(SpiralGrid.getGridAdjustment(1, direction), new Dim(1, 0));
        assertEquals(SpiralGrid.getGridAdjustment(3, direction), new Dim(0, 1));
        assertEquals(SpiralGrid.getGridAdjustment(5, direction), new Dim(-1, 0));
        assertEquals(SpiralGrid.getGridAdjustment(7, direction), new Dim(0, -1));
    }

    @Test
    public void test_DownLeft_Spiral() {
        Dim direction = new Dim(-1, 2);
        assertEquals(SpiralGrid.getGridAdjustment(1, direction), new Dim(0, 1));
        assertEquals(SpiralGrid.getGridAdjustment(3, direction), new Dim(-1, 0));
        assertEquals(SpiralGrid.getGridAdjustment(5, direction), new Dim(0, -1));
        assertEquals(SpiralGrid.getGridAdjustment(7, direction), new Dim(1, 0));
    }

    @Test
    public void test_LeftDown_Spiral() {
        Dim direction = new Dim(-2, 1);
        assertEquals(SpiralGrid.getGridAdjustment(1, direction), new Dim(-1, 0));
        assertEquals(SpiralGrid.getGridAdjustment(3, direction), new Dim(0, 1));
        assertEquals(SpiralGrid.getGridAdjustment(5, direction), new Dim(1, 0));
        assertEquals(SpiralGrid.getGridAdjustment(7, direction), new Dim(0, -1));
    }
}
