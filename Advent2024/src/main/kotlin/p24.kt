package main.kotlin

import java.io.File

fun main() {
    var isReadingWires = true
    val wires = mutableMapOf<String, Boolean>()
    val rules = mutableSetOf<List<String>>()
    File("inputs/input24modified.txt").forEachLine { // Note modified input for part B
        val line = it.split(" ")
        if (it.isBlank()) {
            isReadingWires = false
        } else if (isReadingWires) {
            wires[line[0].removeSuffix(":")] = line[1] == "1"
        } else {
            rules.add(listOf(line[0], line[1], line[2], line[4]))
        }
    }
        println("Wires: $wires")
        println("Rules: $rules")

    // Credits to https://www.reddit.com/r/adventofcode/comments/1hl698z/comment/m3k68gd/?utm_source=share&utm_medium=web3x&utm_name=web3xcss&utm_term=1&utm_content=share_button
    for (rule in rules) {
        if (rule[1] == "XOR" && rule[0].first() !in setOf('x', 'y') && rule[3].first() != 'z') {
            println("X, Y Inputs should not map to outputs: $rule")
            // fgc - z12
            // mtj - z29
            // dtv - z37
        }

        if (rule[1] == "XOR" && rule[0].first() in setOf('x', 'y') && rule[3].first() == 'z') {
            println("Z outputs should not come from direct inputs: $rule")
            // x00 is an exception
        }
        if (rule[1] != "XOR" && rule[3].first() == 'z') {
            println("Outputs should come from xor: $rule")
            // z45 is an exception
        }

        // For graphviz https://dreampuf.github.io/GraphvizOnline/?engine=dot&compressed=CYSw5gTghgDgFgUgEwAYDiyUIOwCEEoCCAZgMYDumAtAgMwCimhAcgCID6AVp5UXBADtqdRqhYduvQuK49hDTJIKEI-eaKJt2ETgGdlYcsHVMtO-URnmTqa0QCeSACw2UADQDyAJXYCYxZQAPZ1dPHz8AojDff1cI5QAXAU5Q73YALwBGTOUAR1yhVBoFVGisnKi08tdqok5iCFc0zggANzy4AKKRTGa25T727pKUFvaiYjBSVxkwYEaicjhp4Y1Z%2BeV1xtXMOYXCYFyAWxmtI6PEImBiSh2xM4vNh8Q7lHPLwnsUAA5UnwhcmAgj8-tpAcpogCga8ocpyK1tihihoyihaMpdMRcqD0miIVU8a9ceiHEgAGyggQAI2xRGCFNe0WptMITJpcXZdMyWEZaQErQA1sp7NzKYL8eFxa9%2BUKiIIVkieqUCQBOZSCLqKkaotWVHy4tVElC6g7kfSvGQJXSywicAVU04cK02y3W1zOoK0FwWrRwXQWT5ex3sP0BmSh1wRohgAUK5G9HykYgOogJYia%2BOoNJJlOEbPJ1w55TvJo%2BGn7VpwW5ajRpcsDMu5RGZlD1oitYApH0cGPTPitbHd9i9p492OuEdEAXEGCl9j%2BYx8f1zhcN%2BfEYyvVd8KnQmumCTLdXkIVDzhHyxac9xpWjC%2BfJxIYMJKnjQiBR-P1%2Bj9gvob71A-2UOAwBOV40lyO0DBPOdIJtCCoNeODlE4I5m1vGRiExPIrWDLDImkLR8NcYiiBgCBTwA9wqkyCkly7KiyloiUMmYolmNJJ9eR8chSASIIkC4xi0l4-i9XYUTXEkuojDnakA2IVp-xbPkqQDVTzSo%2BTlFIAUGJUnwBXvPS9wM9gjL7PNDKPV4LOUYBIBxWgSRUCBN2E-VnJY9IvKJLy6RQHkqJkZJcy%2BIKWxCzhcyih1pWi4FfiHYAjkXT4QWS1KfxS9yWxy4VMgAVmDAUBCELliqHUrysIjhqtceq6mISjIq0dIkBNQU4uCtqOp-drDSogbhV8jz2CUk46VGltogmli5teOap04WduIyT8iAEI59NvMoNtZKpPyJfaviEma0iUj5AhQM7douyt5oexaHqII4wAzW861aAjDkHKivoIgHXFfAjdAEXKMK0BJWhq3RrmfGGf2hwoqOR9VOAhkZs1aIFU2-cDExxtdSCJ14SdxwgwDgVaxtxOjCASTgb21Al6dRBkhpQem9Jp1qOB0NKtp2kYrAxn8BdcCXXt3OdrQpsBQNlgUKbSOXXDVxYxYJrh7Xs6G5ztXNml115DaCJx0TW%2BYA3sC3QWtliHdeB2iGQ5L8mUMgWshjhfuyj3nY9qakrG4AzRGkPzp8MOA2iGPXHj1MTZ6jhOlnTaEqHNOf2z15s7I%2BG1valxNtiIuQnE4uagr99aA5qPzKUkb67uwym-EgUm9s9v3ycHIh0rNLbf7lPxrgNKZEH1wp-bORtdKybCFz-7DK2tcF4atep0V7WQKkAQBX43fDDXPfIxPhwUGwZ902Ba%2BhzTAjLVv15H5QueV98BJ9k4b65O-tcAgAHSgAXSPuoI4AywcOAtakCKbRDgZGKBtdBoN2uFIewtBUGt3YOgx2NwE4ELpEgSqo9WiyVJKQvm40KG1RoZjDQ5C0rAASLzHBPknx8GAGwlmnlOEHT4TUWg-DAiZGwbw7QugUgODEaCCAUiWLyOFhoJRwo7YP2ALmD8ltR4JE0UjfRr99EBSoTgt60j0qmIkeYliNjXg2MviPahrQ5ABScT7cari6EuOrC2HxwpUCgnIDARewQIo4OCYvaIkSpIhOLGpJy3Mu60zrt5VJfl6bkDciVGc9kmY5PTnQ6cPCNDFOBO4iRCRdaONBFU3M0Q6numqZTDY2sWFSHIMQF4n92lrl6c7BIHS%2BLBiOMg7SQ5RkUxkJM1wMzXYFGDMkD4SRlEHl8OeH8Sy4gbK5PfMaxABAYMyHshuBypDRDOSRQ56oBAfRFmYLKcoBDMzWA8iebzJaPMIFSKkKNqGdwsXZKqLifwAoaiCgKt17kcFyH6YUN1gywrDFoJFrhUVEDVuXFyVJlY4iQC5Mo%2BLq4uTmHclEh0sBEHrEXJwlKBHrXCSMdItL1TgizmAMShAizss5eGDl59OXBBOTg%2B0BFHDCokaKliUrbJUgInMZSHjciH2AgKBhazlW8pRSqpCKquTelHrpCxIoDXUKNT%2Bc1ZM9LKDBdrciNp6p2oomue1rhXWpkRm01h6o4DeyxtHb1RA0gsJKZgENnspg4kyC5JYLzMBMQJTRHRLYsguW2ppMyqh9gUG6pmtQQb-hqBhPm98xpnyoXheI15ToK2XhrWBVGtbCDBNzZ9fUThioTCpKsrM7bO1WXWlYjQzL%2B2OGTR4sAug3zBHHdC4cU6fyTsVSMJdAkh3xuzF8xw67lSJi%2BdEUgWUyZfKdrTO2RByY4nPfS5ls7h3XtEXejdPF7wiifbuiS95ok2SorG5QTC5zBM5bkKkcbe0SRgJykSkHYmco3kXBF7Ynq00QzehFRp%2BEikjh43%2BBFRHYbnbhn8RHTZ-1JO%2B6iZZcXEIo9EHF8C6y4tePR5QtyG3UOTBYw5viPGcZ-Hx14fG5RFtpkgX40ZZLl3EzesT1dpOKTJQmEMA5WOkD%2BW25TLI0hwBU3nFTF6CwIek7CIz3kMqc2k927pZkrQEVA4p8Dtm1xOdfthK4a8i6ik2lKWmXmb2inYnSzBFGZAOX2IEaaHiwvZUcs7RyMiQtaE6VdaNwZks-nS68dLiwYNVUVptdkeXF4yGVux28pW1Gmpwdx82VWJE1fEjV6U5ApCIJpRUFQMF2veRgUNcBqYK1tK3ioI9PThvBo81RYAw2vgtznc8mq105vVt8GpzZa3pRrc4sGBWoTBI7fy3Q3bE5DuYj%2BtQkCi94ToTnZdnOO8qJ3apX-Ic8iap2NHm98WYNJY-aQ9Z3j8IbWTY40Dut7AbjLo0JDuEcTy50pc6JulhLGXDsCQ4WgFTyU%2BDBmlCLWPn3sFxyxYnrxicBTq9jiHul4WU8J17FiDPBM0%2BgVCqnPNzZs8JxzjuK0Gp86uN6oc5ABAU3HqG%2B4HARdTKS6LqScu%2BAn3LtfKccq8Uq5kxKtHGvUI3ap5MSyuRcJrQN4zyNgnI2kgJx%2BmAegBLW8o-OO34lbcZtvK7gSdPJdcAaAEr3mgJC%2B-B-UPXigg-NpltrBVxZGZzmjwW4cwAoe7CT8oVcQ5dC4T4BAVtc7M-Io4Pn9WWeSDYW1lkfhAhchu-9axfhNEueoAr8CVHhPcRBcCjiTvldO9GjpTintAeJJiwmHAc7HjyAj7oZP9VqAZ-2XkTt3iOlSaj0MJZWYy-Xjr-VD9qP1N1RkDjwfhPVMJcoDP8CKthONSVrkbcxRD%2BYQP7lGqucXSWRgFaGV2vH%2B1x-8EzH1Y0zhQw60aQQw61RAd1xA62vDkm2n-XHngIsT5AQOlAQMWBzyjRLgOEGWwO8kyH9yyBwKw1BDICkHwzIIoEZ2oOZwoOyB21twKgd1mCYPBzACYO3zYPSmW0J0ZksiW1qXyXEn4PdGEM%2BEyEbyHyrwDFESkJCmr02UUOlEUNJGv293IGQWCHUKH00Jlyl0j1-WQRFF4I-T9AsVEVMMd3MJYhsLzgUQK3HwkXakyVy1EzZiqHJGrnpiFWDBzQCS1zWX8PB38LJk0JGh3WsKwKmkiIQWiPpX4Fzw0ESPslizIQSEsggGyQHgyJ-FaFyMWlyIxwIxW3ICOFkNoBKLWTKILwknKKknqNTFyHP200EGLG4DnH4BqlaPUxGC6LT0LlHlhQIniCHGGJ-HGKQk6DyB1U-jhgIkBCPjmPhgT3mPVhWPfFkzPFaEKUcCqO91-kKRkEONcBONdjSLMm-32CpiWMuLQjXCuOnnuIxysKsDfymleLMHeLoQolnxQF%2BMSDdDaWODhHqOBMXmDRBMDlCWER2zwIx3kK0A5SkFmDwO33hMpgXTtQ2LaB41rxgA2LSAJIcxQGJIKkCO9wSGaKCGOWfGpPBypPP0ZNYwSXLn4UkjZO8n2yJH228z9RW1IDABqmmx-wFKFItXFLJnFPbHuPLg6w9zlK5OgKQDALLyGPi2bTh3VP2BkEBFD1QD1PhUSx7BggCmNOHFNKOy6yokMBtGpVHj8E5VJUWRg3B0dLiFdM%2BC8KHAPkFW9IdL1ToV9LiEDNUU8xNCAX1MdyyBNCYh0JjIKh0JkHsxpKTK0BTPBxTOYyTF30H2TJ2UIEjODCsx-BLOYwLN9T%2BLKFoDpWjyLhrLSVbybwbPbGTgbiOFjyan5MJw7IsWiF7NmU7IOFG0zWeWAn5W1nlDXCnOfwELYjXywgKk%2BJ7EXPYMXO31XNLX92mXelpxGV3PBzehJKPPNibMd2dOgTPOiAvPpWdO3w2JFDPOmSJi5CfLOBfLoSOFXxbC-Ipi%2BEiKsCPxMWDAgCAp%2BKPxhDAq%2BFo1VixGBBgpxzgvEjO3ViQs2P2PPNoT2NBEMDSmvMkxtNoSySrKqCDCuHyXrJwOrKILIu%2BWtStn1lHycKpxYTfDjn1gGTfA7EH1RA1x02YrbyvjMwpJQFxA1w4MH2aGeMLI2W1l1zXHktNmkvjnrOkzNlUrSQwp8mkwWk-mIv-QKL0uyQT30qy2MttB2INmanRhr1rB8HqHgnsustNmspkViLrF%2BRpPcrLE8vEh%2BV6I0H8pGn9zjnEIixCuDXENCrAxQE7Esi%2BBEtmk7DvlBG4vmmSsWmSs2iY0-ggEtPdMnMtLSDyu7NsEtLPgmWd0IBlVHnTR-DqvsSqpFCkOiCN1kMkNBDapYi6qQhL2lznCyLSmRIGvMuKuyKokGpQn6HLn7UPRIv1BIS5MiPan7QVNHkUi4tlPWp7kwmSRbA2uUCCs837SBV837SYmWqKgxGN0-lGQDGtCjLSDurXGevsQSSmjfJhX5Qx0%2BvYEWImInKon%2Box0SsYwsNoFBqoz7LBuBmtU2hayvRwLY0Rp6yIKcBwJY1e3IQxByuoVxPFmxphGxoRIgW4U9Bau0zJvEnF0jCpsIFRW1mu09iB0ZoRDXCZqyzZo%2BogW%2BMwSvO02%2BIQTfzzl5vRrSxgC0TFuFwloyxlqyxluIV%2BpuICSVrgC1R7DVonE1oxzTI4ErAoKwWDH1ryKrGnlNquDmCjQ10uU8w1yYhEqyA11EKHAUwMAPxdrIzoVdsE09pNSCXiNEQip4niOiSwLMtzBttqo6Itr%2BOmWjs-I6MaosTWPrI61JxSUgNIugMx3iVsrWS-0Xk7mT29wLsXW-wnHLqmhYLMF9RGmrv5lrvB1UFKv%2BMbtrhEpkB8g10wQ7raghv6n7r8g1w%2BwblxH7RBi73OoJGWpQH7W-jzvAwciGvXJ6UtoTyXoTjXqXjDpSVWo3Ccins8mWtoH7SySSLWRMj1jxJW0vvB0vtsk4AplEQd1aoWRkRfogjfvpXyACswB-s9loN82k36M82kyYi0syGk0wQAq0Aes9BgcLwFB1NgaQfVlQbUNSuaW0MwfqQujbNvC6lPMWWowfF%2BoPn0N8FxtvHIbT2dQfkX3omfAYboXnvdGYbmsRVAymuLqHxAw3xRS4aQi4Ykxir5GIEXj0XPvA2RoT2RulHEbyEQhQxwKZ2UbMyIJQBwMOFFLb0NquAIXrNjNIvjL0a9Prq4FoWCHMZ4HeQkAIpbBseLG-I0xWR0klM-lcYT1cdfmSGUBwFYCAA
        //                println("${rule[0]} -> ${rule[1] + "_" + rule[3]}")
        //                println("${rule[2]} -> ${rule[1] + "_" + rule[3]}")
        //                println("${rule[1] + "_" + rule[3]} -> ${rule[3]}")
    }

    outer@ while (rules.isNotEmpty()) {
        rules.forEach { rule ->
            if (rule[0] in wires && rule[2] in wires) {
                wires[rule[3]] = when (rule[1]) {
                    "AND" -> wires[rule[0]]!! && wires[rule[2]]!!
                    "OR" -> wires[rule[0]]!! || wires[rule[2]]!!
                    "XOR" -> wires[rule[0]]!! xor wires[rule[2]]!!
                    else -> throw Exception("Illegal boolean bitwise op")
                }
                rules.remove(rule)
                continue@outer // To get access get kotlin compiler 2.2 https://stackoverflow.com/questions/33287227/how-can-i-change-the-kotlin-compiler-version-on-intellij#:~:text=Go%20to%20Intellij%20Preferences%20%2D%3E%20Build,to%20the%20one%20you%20wish.
            }
        }
    }

    var current = 0L
    for (zkey in wires.keys.filter { it.startsWith("z") }.sorted().reversed()) {
        current = current shl 1
        if (wires[zkey]!!) {
            current += 1
        }
    }
    println("Part A: $current")

    /**
     * Idea: If we fill x,y manually in our input with all 1s we can identify the problematic bits, either at this level
     * or the prior
     *
     * Looks like an x,y -> AND -> next OR was violated by dgr, vvm
      */
    //    println(current.toString(2))
    //    current.toString(2).forEachIndexed { index, ch ->
    //        if (ch != '1') {
    //            println(45 - index)
    //        }
    //    }

    val partB = listOf("dgr", "vvm", "fgc", "z12", "mtj", "z29", "dtv", "z37").sorted().joinToString(",")
    println("Part B: $partB")


}