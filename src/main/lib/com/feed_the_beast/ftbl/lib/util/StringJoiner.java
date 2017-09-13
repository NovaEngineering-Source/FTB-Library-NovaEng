package com.feed_the_beast.ftbl.lib.util;

import java.util.Collection;

/**
 * @author LatvianModder
 */
public abstract class StringJoiner
{
	public static StringJoiner with(String string)
	{
		switch (string.length())
		{
			case 0:
				return WithString.WITH_NOTHING;
			case 1:
				return with(string.charAt(0));
			default:
				return new WithString(string);
		}
	}

	public static StringJoiner with(char character)
	{
		switch (character)
		{
			case ',':
				return WithChar.WITH_COMMA;
			case ' ':
				return WithChar.WITH_SPACE;
			default:
				return new WithChar(character);
		}
	}

	private static class WithString extends StringJoiner
	{
		private static final WithString WITH_NOTHING = new WithString("");

		private final String s;

		private WithString(String _s)
		{
			s = _s;
		}

		@Override
		protected void append(StringBuilder builder)
		{
			builder.append(s);
		}
	}

	private static class WithChar extends StringJoiner
	{
		private static final WithChar WITH_COMMA = new WithChar(',');
		private static final WithChar WITH_SPACE = new WithChar(' ');

		private final char c;

		private WithChar(char _c)
		{
			c = _c;
		}

		@Override
		protected void append(StringBuilder builder)
		{
			builder.append(c);
		}
	}

	protected abstract void append(StringBuilder builder);

	public String join(Object[] objects)
	{
		StringBuilder builder = new StringBuilder();
		boolean first = true;

		for (Object object : objects)
		{
			if (first)
			{
				first = false;
			}
			else
			{
				append(builder);
			}

			builder.append(object);
		}

		return builder.toString();
	}

	public String join(Iterable objects)
	{
		StringBuilder builder = new StringBuilder();
		boolean first = true;

		for (Object object : objects)
		{
			if (first)
			{
				first = false;
			}
			else
			{
				append(builder);
			}

			builder.append(object);
		}

		return builder.toString();
	}

	public String join(Collection objects)
	{
		return objects.isEmpty() ? "" : join((Iterable) objects);
	}
}