package com.example.habisin.util

/**
 * Hand-picked, exact photos per catalog dish (direct image links from asset.kompas.com).
 * Empty = fall back to the keyword photo. Keyed by the exact catalog name from GET /api/catalog.
 */
val CURATED_RECIPE_IMAGES: Map<String, String> = mapOf(
    "Nasi Goreng Kampung"  to "https://asset.kompas.com/crops/VMupLYa-zBYTf5h5GnEjMW6-Nxg=/0x0:1000x667/1200x800/data/photo/2020/11/22/5fba747cef43d.jpg",
    "Telur Balado"         to "https://asset.kompas.com/crops/z9tXehGd66Vio4CPWi0aQJ6uzMY=/100x67:900x600/1200x800/data/photo/2023/05/04/64533129bf231.jpg",
    "Capcay Kuah"          to "https://asset.kompas.com/crops/jV1_Hp2WkEM0-uxzuMjFz9-q6rU=/6x2:700x465/1200x800/data/photo/2021/11/04/6183c80555018.jpg",
    "Tumis Kangkung Terasi" to "https://asset.kompas.com/crops/p45tRPhU0i3KmJe-L9YjQMgE7g4=/9x5:797x398/1200x800/data/photo/2017/05/27/50118613.jpg",
    "Tempe Orek Kering"    to "https://asset.kompas.com/crops/GG1NpGCGxPRga5KdxhRwVP04I1I=/0x61:909x667/1200x800/data/photo/2022/03/31/6244fcda6bc51.jpg",
    "Sayur Asem"           to "https://asset.kompas.com/crops/3Ydj-WUV9nXFfk-n9p6ziL2sC_k=/100x67:900x600/1200x800/data/photo/2024/05/07/66397565611b0.jpg",
    "Pisang Goreng"        to "https://asset.kompas.com/crops/eABDv6XX5_YQPkqFA7gVfmtXq6E=/0x0:1056x704/1200x800/data/photo/2025/09/24/68d37921c8b99.jpg",
    "Bakwan Sayur"         to "https://asset.kompas.com/crops/uA9-5lQ7CQwvaVPkEYiAJPijmBU=/0x0:1000x667/1200x800/data/photo/2023/06/13/6487fc3a3c5ef.jpg",
    "Es Teh Manis"         to "https://asset.kompas.com/crops/toOljW__-UqEVhGAJe8UyPdZWnU=/92x67:892x600/750x500/data/photo/2023/08/23/64e59deb79bfb.jpg",
    "Wedang Jahe"          to "https://asset.kompas.com/crops/fRZ5wL-AVIgaJrfNXsjvXandg1g=/2x0:700x465/1200x800/data/photo/2024/05/05/6636d4404b3e9.jpg",
    "Sambal Terasi"        to "https://asset.kompas.com/crops/_7_dgwSPMDChSnnddNlkwtpJyrI=/116x66:910x596/1200x800/data/photo/2023/08/15/64db616a8a03d.jpg",
    "Sambal Bawang"        to "https://asset.kompas.com/crops/C9Bu__2vqaMbbvIVGL7AuKKBDPc=/3x0:700x465/1200x800/data/photo/2020/10/09/5f7fec4cc024d.jpg",
    "Rendang Daging Sapi"  to "https://asset.kompas.com/crops/B62efXYwZ7SGcbEB8Lw7vTQrIkg=/100x67:900x600/1200x800/data/photo/2020/06/30/5efb2f520efb4.jpg",
    "Soto Ayam Lamongan"   to "https://asset.kompas.com/crops/9TkjfcRInG7sZYsqzyKU8qLlKVU=/0x1:1000x668/1200x800/data/photo/2021/09/26/614fd4f197446.jpeg",
    "Ayam Goreng Lengkuas" to "https://asset.kompas.com/crops/NzEV5TayMdlh3_2RIRoncBFSmLs=/160x87:887x572/1200x800/data/photo/2023/06/06/647e93f68b845.jpg",
    "Perkedel Kentang"     to "https://asset.kompas.com/crops/O14vS4czsNPqpcC8Q0m9axHD7jI=/0x13:3210x2153/1200x800/data/photo/2025/11/17/691a9abf2ba25.jpg",
)

fun curatedRecipeImage(name: String?): String? =
    name?.let { CURATED_RECIPE_IMAGES[it.trim()] }?.takeIf { it.isNotBlank() }
