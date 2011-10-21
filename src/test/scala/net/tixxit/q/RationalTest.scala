package net.tixxit.q

import junit.framework._
import Assert._

object RationalTest {
  def suite: Test = {
    val suite = new TestSuite(classOf[RationalTest])
    suite
  }

  def main(args: Array[String]) {
    junit.textui.TestRunner.run(suite)
  }
}

class RationalTest extends TestCase("rational") {
  def testRationalCanonicalConstruction() = {
    val r = Rational(5,6)
    assertEquals(r.numerator, BigInt(5))
    assertEquals(r.denominator, BigInt(6))
  }

  def testRationalDegenerateConstruction() = {
    val r = Rational(30, 345)
    assertEquals(r.numerator, BigInt(2))
    assertEquals(r.denominator, BigInt(23))
  }

  def testRationalIsFractionalImplicitExists = {
    def doStuff[NT](a: NT, b: NT)(implicit n: Fractional[NT]) = {
      import n._
      a / b	// Note: a & b are of unknown type NT, not Rational
    }
    assertEquals(doStuff(Rational(1), Rational(2)), Rational(1, 2))
  }

  def testEquivalentCanonicalAndDegenerateRationalsAreEqual = {
    val a = Rational(1, 2)
    val b = Rational(8, 16)
    assert(a == b)
  }

  def testNonEquivalentRationalsAreNotEqual = {
    val a = Rational(1, 2)
    val b = Rational(1, 3)
    val c = Rational(2, 1)
    
    assertFalse(a == b)
    assertFalse(a == c)
  }
  
  def testComparisons = {
    val a = Rational(1, 2)
    val b = Rational(3, 4)
    val c = Rational(-1, 2)
    val d = Rational(1, 2)
    assert(a < b)
    assert(b > a)
    assert(a > c)
    assert(c < a)
    assert(a <= d)
    assert(a >= d)
  }

	def testPrimitiveComparisons = {
		val a = Rational("5000000000")
		val b = Rational(-123456)
		val c = Rational(1, 8)
		assert(a == 5000000000L)
		assert(5000000000L == a)
		assert(b == -123456)
		assert(-123456 == b)
		assert(c == .125)
		assert(.125 == c)
		assert(c == .125f)
		assert(.125f == c)
	}

  def testAddition = {
    val a = Rational(3, 10)
    val b = Rational(4, 19)

    // This will go through the coprime denominator path.
    // Since, 97 and 190 are coprime, 97/190 is canonical too.
    assertEquals(a + b, Rational(97, 190))

    val c = Rational(1, 2)
    val d = Rational(1, 6)
    // This will go through the non-coprime denominator path. Since the
    // GCD of 2 and 6 is 2, the numerator 1 * 3 + 1 * 1 = 4 is tried first.
    // The GCD of 4 and 2 is 2, so the numerator will need to be reduced.
    assertEquals(c + d, Rational(1 * 6 + 1 * 2, 2 * 6))

    val e = Rational(1, 2)
    val f = Rational(3, 4)
    // This will go through the non-coprime denominator path. Since the
    // GCD of 2 and 4 is 2, the numerator 5 is tried first, which is
    // coprime with 2, so the numerator need not be reduced.
    assertEquals(e + f, Rational(1 * 4 + 3 * 2, 2 * 4))
  }

  def testSubtraction = {
    // Just ripped from addition
    val a = Rational(3, 10)
    val b = Rational(4, 19)
    assertEquals(a - b, Rational(3 * 19 - 4 * 10, 10 * 19))

    val c = Rational(1, 2)
    val d = Rational(1, 6)
    assertEquals(c - d, Rational(1 * 6 - 1 * 2, 2 * 6))

    val e = Rational(1, 2)
    val f = Rational(3, 4)
    assertEquals(e - f, Rational(1 * 4 - 3 * 2, 2 * 4))
  }

  def testMultiplication = {
    val a = Rational(2, 3)
    val b = Rational(1, 2)
    assertEquals(a * b, Rational(1, 3))

    val c = Rational(-321, 23)
    val d = Rational(23, 13)
    assertEquals(c * d, Rational(-321 * 23, 23 * 13))

    val e = Rational(-1, 2)
    assertEquals(e * e, Rational(1, 4))
  }

  def testDivision = {
    val a = Rational(2, 3)
    val b = Rational(1, 2)
    assertEquals(a / b, Rational(4, 3))

    val c = Rational(-21, 5)
    val d = Rational(7, 18)
    assertEquals(c / d, Rational(-54, 5))

    val e = Rational(-23, 19)
    assertEquals(e / e, Rational(1))
  }

	def testPow = {
		val a = Rational(1, 2)
		assertEquals(a pow 32, Rational(1, BigInt("4294967296")))

		val b = Rational(-3, 1)
		assertEquals(b pow 2, Rational(9, 1))
		assertEquals(b pow 3, Rational(-27, 1))
	}

	def testLongValue = assertEquals(Rational("5000000000").toLong, 5000000000L)
	def testIntValue = {
		assertEquals(Rational(3).toInt, 3)
		assertEquals(Rational(-5, 2).toInt, -2)
	}
	def testShortValue = {
		assertEquals(Rational(65535).toShort, -1)
		assertEquals(Rational(65536).toShort, 0)
		assertEquals(Rational(-5).toShort, -5)
	}
	def testByteValue = {
		assertEquals(Rational(-1).toByte, -1)
		assertEquals(Rational(256).toByte, 0)
	}
	def testToDoubleAndFloat = {
		assertEquals(Rational(1, 2).toFloat, 0.5f)
		val a = Rational("10000000000000002/10000000000000000")
		assertEquals(a.toDouble, 1.0000000000000002)
		assertEquals(a.toFloat, 1.0f)
    assertEquals(Rational(2, 3).toDouble, 2 / 3.0)
	}

  def testToString = {
    assertEquals(Rational(1, 2).toString, "1/2")
    assertEquals(Rational(1, -2).toString, "-1/2")
    assertEquals(Rational(2, 4).toString, "1/2")
  }

  def testHashCodeIsSameForEquivalentRationals = {
    assertEquals(Rational(1, 2).hashCode, Rational(2, 4).hashCode)
    assertEquals(Rational(0).hashCode, Rational(0, 5).hashCode)
    assertEquals(Rational(-1, 2).hashCode, Rational(1, -2).hashCode)
  }
}
