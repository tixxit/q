Rationals
=========

This is an implementation of rational numbers in Scala that integrates well
with Scala's other number types (`Int`, `Long`, `Double`, `Float`, `BigInt`, 
`BigDecimal`, etc.).


Why use it?
-----------

It uses `BigInt` for its integer type, so it has high "precision".

All `Rational`s are in canonical form; the numerator and denominator are
coprime and the denominator is always positive.

It integrates well with Scala's number types by implementing ScalaNumber and
ScalaNumericConversions (eg. `1 == Rational(1)`, `Rational(1) == 1`, 
or `Rational(1,3) == 1 / 3.0`).

It supports ranges like `Rational(0) to 1 by Rational(1,3)`.

Supports Numeric (eg. `(Rational.zero to 1 by Rational(1,3)) sum`)

The operations (+, -, *, /, pow) try to be faster than the naive
implementations where possible.


Using it.
---------

    import net.tixxit.q.{Rational => Q}

    val a = Q(1, 3)         // Makes a rational 1/3.
    val b = Q("-1234/5678") // Construct from strings.
    val c = Q(1.234)        // Make it a double.
    // etc.
    
    assert(a.inverse == Q(3, 1))
    assert((a pow 3) == Q(1, 27))
    assert(a * 3.4 - Q(1,2) == Q(19, 30))
    assert(Q(1, 2) < Q(5, 8))


Why BigInt?
-----------

The better question is why don't we use `Int` or `Long`:

1. If you care enough to use rationals over fp, then you probably want precision.
2. Overflows are hard to reason about with rationals (vs. `Int` and `Long`).
3. Rational arithmetic is not super fast, so the speed increase would be dubious.

