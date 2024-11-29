package kr.open.rhpark.library.util.extensions.collection

/**
 *  ex)
 *  data class Person(val name: String, val age: Int)
 *
 * val people = listOf(
 *     Person("Alice", 30),
 *     Person("Bob", 25),
 *     Person("Charlie", 30),
 *     Person("David", 25)
 * )
 *
 * // 나이를 기준으로 중복 제거
 * val distinctPeopleByAge = people.distinctBy { it.age }
 */
public fun <TYPE, RES> List<TYPE>.distinctBy(selector: (TYPE) -> RES): List<TYPE> {
    val seen = mutableSetOf<RES>()
    return filter { seen.add(selector(it)) }
}


public fun <TYPE> MutableList<TYPE>.swap(index1: Int, index2: Int) {
    if (index1 in 0 until size && index2 in 0 until size && index1 != index2) {
        val tmp = this[index1]
        this[index1] = this[index2]
        this[index2] = tmp
    }
}