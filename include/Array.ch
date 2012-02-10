/* cowbel test suite
 *
 * Written in 2012 by David Given.
 *
 * To the extent possible under law, the author has dedicated all copyright
 * and related and neighboring rights to this software to the public domain
 * worldwide. This software is distributed without any warranty.
 *
 * Please see the file COPYING.CC0 in the distribution package for more
 * information.
 */

#ifndef COWBEL_ARRAY
#define COWBEL_ARRAY

type Array<T> =
{
	function get(i: int): T;
	function set(i: int, value: T);
	function length(): int;
};

function Array<T>(size: int, initialiser: T): Array<T>
{
	var ptr: __extern = 0;
	extern "${ptr} = calloc(${size}, sizeof(${initialiser}));";
	
	return
	{
		implements Array<T>;
		
		function get(i: int): (result: T)
		{
			extern "${result} = ((typeof(${result})*)${ptr})[${i}];";
		}
		
		function set(i: int, value: T)
		{
			extern "((typeof(${value})*)${ptr})[${i}] = ${value};";
		}
		
		function length(): int
		{
			return size;
		}
	};
}

#endif
