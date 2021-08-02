package me.line.notifybot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.Objects;

/**
 * 由兩個點構成的線段。
 *
 * @author P-C Lin (a.k.a 高科技黑手)
 */
public class Distance implements Serializable {

	private static final long serialVersionUID = -986188697186707886L;

	/**
	 * 原点
	 */
	private static final Point2D origin = new Point2D.Double();

	/**
	 * the start Point2D of this line segment
	 */
	private Point2D p1;

	/**
	 * the end Point2D of this line segment
	 */
	private Point2D p2;

	/**
	 * @param degree 角度
	 * @param start 方位角度開始範圍(大於等於)
	 * @param end 方位角度結束範圍(小於)
	 * @return 布林值
	 */
	private boolean isBetween(double degree, double start, double end) {
		return degree >= start && degree < end;
	}

	/**
	 * @return 若為水平線段，回傳 true。
	 */
	private boolean isHorizontal() {
		return p1.getY() == p2.getY();
	}

	/**
	 * @return 若為垂直線段，回傳 true。
	 */
	private boolean isVertical() {
		return p1.getX() == p2.getX();
	}

	/**
	 * 羅盤方位
	 */
	public static enum CardinalDirection {

		/**
		 * 0°
		 */
		CENTER,
		/**
		 * 北北東 11.25°~33.75°
		 */
		NORTH_NORTHEAST,
		/**
		 * 北東 33.75°~56.25°
		 */
		NORTHEAST,
		/**
		 * 東北東 56.25°~78.75°
		 */
		EAST_NORTHEAST,
		/**
		 * 東 78.75°~101.25°
		 */
		EAST,
		/**
		 * 東南東 101.25°~123.75°
		 */
		EAST_SOUTHEAST,
		/**
		 * 南東 123.75°~146.25°
		 */
		SOUTHEAST,
		/**
		 * 南南東 146.25°~168.75°
		 */
		SOUTH_SOUTHEAST,
		/**
		 * 南 168.75°~191.25°
		 */
		SOUTH,
		/**
		 * 南南西 191.25°~213.75°
		 */
		SOUTH_SOUTHWEST,
		/**
		 * 南西 213.75°~236.25°
		 */
		SOUTHWEST,
		/**
		 * 西南西 236.25°~258.75°
		 */
		WEST_SOUTHWEST,
		/**
		 * 西 258.75°~281.25°
		 */
		WEST,
		/**
		 * 西北西 281.25°~303.75°
		 */
		WEST_NORTHWEST,
		/**
		 * 北西 303.75°~326.25°
		 */
		NORTHWEST,
		/**
		 * 北北西 326.25°~348.75°
		 */
		NORTH_NORTHWEST,
		/**
		 * 北 348.75°~11.25°
		 */
		NORTH
	}

	/**
	 * Constructs and initializes a Line with coordinates (0, 0) → (0, 0).
	 */
	public Distance() {
		this(new Point2D.Double(), new Point2D.Double());
	}

	/**
	 * Constructs and initializes a Line2D from the specified Point2D
	 * objects.
	 *
	 * @param p1 the start Point2D of this line segment
	 * @param p2 the end Point2D of this line segment
	 */
	public Distance(Point2D p1, Point2D p2) {
		this.p1 = p1;
		this.p2 = p2;
	}

	/**
	 * Constructs and initializes a Line2D from the specified coordinates.
	 *
	 * @param x1 the X coordinate of the start point
	 * @param y1 the Y coordinate of the start point
	 * @param x2 the X coordinate of the end point
	 * @param y2 the Y coordinate of the end point
	 */
	public Distance(double x1, double y1, double x2, double y2) {
		p1 = new Point2D.Double(x1, y1);
		p2 = new Point2D.Double(x2, y2);
	}

	@Override
	public int hashCode() {
		int hash = 5;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Distance other = (Distance) obj;
		if (!Objects.equals(this.p1, other.p1)) {
			return false;
		}
		if (!Objects.equals(this.p2, other.p2)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		try {
			return new JsonMapper().writeValueAsString(this);
		} catch (JsonProcessingException ignore) {
			return Objects.isNull(p1) && Objects.isNull(p2) ? "null" : "";
		}
	}

	/**
	 * @return the start point of the line segment
	 */
	public Point2D getP1() {
		return p1;
	}

	/**
	 * @param p1 the start point of the line segment
	 */
	public void setP1(Point2D p1) {
		this.p1 = p1;
	}

	/**
	 * @return the end point of the line segment
	 */
	public Point2D getP2() {
		return p2;
	}

	/**
	 * @param p2 the end point of the line segment
	 */
	public void setP2(Point2D p2) {
		this.p2 = p2;
	}

	/**
	 * @return 角度
	 */
	@SuppressWarnings("UnusedAssignment")
	public double getDegree() {
		double degree,
			x1 = p1.getX(),
			y1 = p1.getY(),
			x2 = p2.getX(),
			y2 = p2.getY();
		if (isHorizontal()) {
			if (x1 < x2) {
				degree = 90D;
			}
			if (x2 < x1) {
				degree = 270D;
			}
		}
		if (isVertical()) {
			if (y2 < y1) {
				degree = 180D;
			}
		}

		Point2D point2D = new Point2D.Double(
			p2.getX() - p1.getX(),
			p2.getY() - p1.getY()
		);
		final double x = point2D.getX(), y = point2D.getY();
		degree = Math.toDegrees(Math.atan2(
			(x - origin.getX()),
			(y - origin.getY())
		));
		if (x < 0) {
			degree = y < 0D ? 90D - degree : Math.abs(360D + degree);
		}
		return degree;
	}

	/**
	 * @return 羅盤方位
	 */
	public CardinalDirection getCardinalDirection() {
		if (isHorizontal() && isVertical()) {
			return CardinalDirection.CENTER;
		}

		final double degree = getDegree();
		if (isBetween(degree, 11.25D, 33.75D)) {
			return CardinalDirection.NORTH_NORTHEAST;
		}
		if (isBetween(degree, 33.75D, 56.25D)) {
			return CardinalDirection.NORTHEAST;
		}
		if (isBetween(degree, 56.25D, 78.75D)) {
			return CardinalDirection.EAST_NORTHEAST;
		}
		if (isBetween(degree, 78.75D, 101.25D)) {
			return CardinalDirection.EAST;
		}
		if (isBetween(degree, 101.25D, 123.75D)) {
			return CardinalDirection.EAST_SOUTHEAST;
		}
		if (isBetween(degree, 123.75D, 146.25D)) {
			return CardinalDirection.SOUTHEAST;
		}
		if (isBetween(degree, 146.25D, 168.75D)) {
			return CardinalDirection.SOUTH_SOUTHEAST;
		}
		if (isBetween(degree, 168.75D, 191.25D)) {
			return CardinalDirection.SOUTH;
		}
		if (isBetween(degree, 191.25D, 213.75D)) {
			return CardinalDirection.SOUTH_SOUTHWEST;
		}
		if (isBetween(degree, 213.75D, 236.25D)) {
			return CardinalDirection.SOUTHWEST;
		}
		if (isBetween(degree, 236.25D, 258.75D)) {
			return CardinalDirection.WEST_SOUTHWEST;
		}
		if (isBetween(degree, 258.75D, 281.25D)) {
			return CardinalDirection.WEST;
		}
		if (isBetween(degree, 281.25D, 303.75D)) {
			return CardinalDirection.WEST_NORTHWEST;
		}
		if (isBetween(degree, 303.75D, 326.25D)) {
			return CardinalDirection.NORTHWEST;
		}
		if (isBetween(degree, 326.25D, 348.75D)) {
			return CardinalDirection.NORTH_NORTHWEST;
		}
		return CardinalDirection.NORTH;
	}
}
