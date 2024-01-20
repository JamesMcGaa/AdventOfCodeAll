import java.lang.Integer.max

const val INFINITY = 123456789
const val BOSS_DAMAGE = 10
const val BOSS_HP = 71
const val PLAYER_HP = 50
const val PLAYER_MANA = 500

var isPartB = false

/**
 * Honest DP - didnt see many others do this approach
 *
 * - Some did brute force with basic pruning
 *
 * - One did Djikstras algorithm
 */
fun main() {
    println(
        minMana(
            WizardState(
                playerHp = PLAYER_HP,
                mana = PLAYER_MANA,
                bossHp = BOSS_HP,
                shieldTimer = 0,
                poisonTimer = 0,
                manaTimer = 0,
                manaSpentSoFar = 0
            )
        )
    )

    isPartB = true
    WizardState.MEMO.clear()

    println(
        minMana(
            WizardState(
                playerHp = PLAYER_HP,
                mana = PLAYER_MANA,
                bossHp = BOSS_HP,
                shieldTimer = 0,
                poisonTimer = 0,
                manaTimer = 0,
                manaSpentSoFar = 0
            )
        )
    )
}

fun minMana(wizardState: WizardState): Int {
    if (wizardState.mana <= 0) {
        return INFINITY
    }
    if (wizardState.bossHp <= 0) {
        return wizardState.manaSpentSoFar
    }
    if (wizardState.playerHp <= (if (isPartB) 1 else 0)) {
        return INFINITY
    }
    if (WizardState.MEMO.contains(wizardState)) {
        return WizardState.MEMO[wizardState]!!
    } else {
        val min = wizardState.getMoves().map(::minMana).min()
        WizardState.MEMO[wizardState] = min
        return min
    }
}

data class WizardState(
    val poisonTimer: Int,
    val shieldTimer: Int,
    val bossHp: Int,
    val playerHp: Int,
    val mana: Int,
    val manaTimer: Int,
    val manaSpentSoFar: Int
) {
    fun getMoves(): List<WizardState> {
        var ret = mutableListOf<WizardState>()
        var current = this.copy()

        if (isPartB) {
            current = current.copy(playerHp = playerHp - 1)
        }

        // Apply Player Timers
        if (poisonTimer > 0) {
            current = current.copy(poisonTimer = poisonTimer - 1, bossHp = bossHp - 3)
        }
        if (manaTimer > 0) {
            current = current.copy(manaTimer = manaTimer - 1, mana = mana + 101)
        }
        if (shieldTimer > 0) {
            current = current.copy(shieldTimer = shieldTimer - 1)
        }

        // Cast Player Spells
        ret.add(
            current.copy(
                mana = current.mana - 53,
                bossHp = current.bossHp - 4,
                manaSpentSoFar = current.manaSpentSoFar + 53
            )
        ) // Magic Missle
        ret.add(
            current.copy(
                mana = current.mana - 73,
                bossHp = current.bossHp - 2,
                playerHp = current.playerHp + 2,
                manaSpentSoFar = current.manaSpentSoFar + 73
            )
        ) // Drain
        if (current.shieldTimer == 0) {
            ret.add(
                current.copy(
                    mana = current.mana - 113,
                    shieldTimer = 6,
                    manaSpentSoFar = current.manaSpentSoFar + 113
                )
            ) // Shield
        }
        if (current.poisonTimer == 0) {
            ret.add(
                current.copy(
                    mana = current.mana - 173,
                    poisonTimer = 6,
                    manaSpentSoFar = current.manaSpentSoFar + 173
                )
            ) // Poison
        }
        if (current.manaTimer == 0) {
            ret.add(
                current.copy(
                    mana = current.mana - 229,
                    manaTimer = 5,
                    manaSpentSoFar = current.manaSpentSoFar + 229
                )
            ) // Mana Regen
        }

        // Apply Boss Timer + Move
        ret = ret.map { preState ->
            preState.copy(
                shieldTimer = max(preState.shieldTimer - 1, 0),
                manaTimer = max(preState.manaTimer - 1, 0),
                mana = if (preState.manaTimer > 0) preState.mana + 101 else preState.mana,
                poisonTimer = max(preState.poisonTimer - 1, 0),
                bossHp = if (preState.poisonTimer > 0) preState.bossHp - 3 else preState.bossHp,
                playerHp = if (preState.shieldTimer > 0) preState.playerHp + 7 - BOSS_DAMAGE else preState.playerHp - BOSS_DAMAGE
            )
        }.toMutableList()

        return ret
    }

    companion object {
        val MEMO = mutableMapOf<WizardState, Int>()
    }
}