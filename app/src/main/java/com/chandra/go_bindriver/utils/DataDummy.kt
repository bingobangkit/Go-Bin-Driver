package com.chandra.go_bindriver.utils

import com.chandra.go_bindriver.model.Order
import java.text.SimpleDateFormat

object DataDummy {
    fun generateOrder(): List<Order> {
        val order = ArrayList<Order>()

        order.add(
            Order(
                "DqAcHlzm8ob7jMZFnXA2",
                "DqAcHlzm8ob7jMZFnXA2/2/3/4",
                "ecG3cQr3aJSzEPnax8pR8K4KhPp1",
                "yrcqexz4wJWwbvU8HvkuVDJVzh12",
                1,
                "Jl. Rancajigang No 128 RT 03 RW 10 Desa Padamulya Kecamatan Majalaya Kabupaten Bandung",
                10,
                200000,
                "-7.061178581414933",
                "107.74810823931362",
                "waiting",
               "2021-12-11"
            )
        )

        order.add(
            Order(
                "DqAcHlzm8ob7jMZFnXA2",
                "DqAcHlzm8ob7jMZFnXA2/2/3/4",
                "ecG3cQr3aJSzEPnax8pR8K4KhPp1",
                "yrcqexz4wJWwbvU8HvkuVDJVzh12",
                1,
                "Jl. Rancajigang No 128 RT 03 RW 10 Desa Padamulya Kecamatan Majalaya Kabupaten Bandung",
                10,
                200000,
                "-7.061178581414933",
                "107.74810823931362",
                "waiting",
                "2021-12-11"
            )
        )

        order.add(
            Order(
                "DqAcHlzm8ob7jMZFnXA2",
                "DqAcHlzm8ob7jMZFnXA2/2/3/4",
                "ecG3cQr3aJSzEPnax8pR8K4KhPp1",
                "yrcqexz4wJWwbvU8HvkuVDJVzh12",
                1,
                "Jl. Rancajigang No 128 RT 03 RW 10 Desa Padamulya Kecamatan Majalaya Kabupaten Bandung",
                10,
                200000,
                "-7.061178581414933",
                "107.74810823931362",
                "waiting",
                "2021-12-11"
            )
        )
        return order
    }



}