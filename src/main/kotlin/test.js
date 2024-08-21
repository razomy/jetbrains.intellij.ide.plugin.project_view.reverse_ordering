// possible solution in javascript, nodejs v22.6.0
function sortByChild(strings) {
    return strings
        .map(s => ({path: s, key: s.split(/[ \-._]|(?=[A-Z])/).reverse()}))
        .sort((a, b) => {
            for (let i = 0; i < Math.min(a.key.length, b.key.length); i++) {
                const a_i = a.key[i];
                const b_i = b.key[i];
                if (a_i !== b_i) {
                    // Java requires an alternative solution like https://github.com/sawano/alphanumeric-comparator
                    return a_i.localeCompare(b_i, undefined, {numeric: true})
                }
            }
            return a.path.length - b.path.length;
        })
        .map((s) => s.path)
}

// testing
function shuffleArray(array) {
    array = [...array]
    for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]];
    }

    for (let i = 0; i < array.length; i++) {
        if (array[i] !== array[i]) {
            console.error(`shuffle return equal: ${array} === ${array2}.repeat.`)
            return shuffleArray(array)
        }
    }

    return array
}

function assertEqualArray(array, array2) {
    for (let i = 0; i < array.length; i++) {
        if (array[i] !== array2[i]) {
            throw new Error(`must be equal: ${array} !== ${array2}`)
        }
    }
    return true;
}

const test_results = [
    [
        ".1.a",
        ".9.a",
        ".10.a",
        ".19.a",
    ],
    [
        ".1.a",
        ".10.a",
        ".9.A",
        ".19.A",
    ],
    [
        ".1.a",
        ".9.a",
        ".1.b",
        ".9.b",
    ],
    [
        ".1.ab",
        ".9.ab",
        ".1.ba",
        ".9.ba",
    ],
    [
        ".a",
        ".ab",
        ".b",
        ".ba",
    ],
    [
        "z.1",
        "y.5",
        "w.9",
        "u.10",
    ],
    [
        "bank.0.txt",
        "adventure.1.txt",
        "book.1.txt",
        "idea.2.txt",
        "cat.4.txt",
    ],
    [
        "a.cs",
        "ba-a.cs",
        "ca-ba-a.cs",
        "da-ba-a.cs",
        "da-ca.cs",
        "ea-da-ca.cs",
    ],
    [
        "a.cs",
        "ba_a.cs",
        "ca_ba_a.cs",
        "da_ba_a.cs",
        "da_ca.cs",
        "ea_da_ca.cs",
    ],
    [
        "a.cs",
        "ba a.cs",
        "ca ba a.cs",
        "da ba a.cs",
        "da ca.cs",
        "ea da ca.cs",
    ],
    [
        "A.cs",
        "BaA.cs",
        "CaBaA.cs",
        "DaBaA.cs",
        "DaCa.cs",
        "EaDaCa.cs",
    ],
    // TODO:
    //  challenge:not handle abbreviation
    //  research:(?=[A-Z]{1:}) research regex for handling Uppercase or write a function
    //  solution:skip
    // [
    //     "a.cs",
    //     "CaAAa.cs", // A
    //     "DaAAAa.cs", // AA
    //     "DaAABa.cs", // AA
    //     "DaCa.cs",
    // ],
    [
        "summer-12-01-2023.png",
        "landing-17-12-2023.png",
        "dog-18-12-2023.png",
        "cat-14-02-2024.png",
    ],
    [
        "Cn.cs",
        "MultiplayerDeviceCn.cs",
        "UserMultiplayerDeviceCn.cs",
        "UserCn.cs",
        "AchievementUserCn.cs",
        "AchievementsUserCn.cs",
        "ChapterUserCn.cs",
        "ChaptersUserCn.cs",
        "FinishUserCn.cs",
        "InfinityUserCn.cs",
    ],
    [
        "string.py",
        "bound_string.py",
        "get_xy_bound_string.py",
        "get_xyz_bound_string.py",
    ]
]

function test() {
    for (let i = 0; i < test_results.length; i++) {
        let test_result = test_results[i];
        const shuffledArray = shuffleArray(test_result);
        const sortedArray = sortByChild(shuffledArray);
        assertEqualArray(sortedArray, test_result)
        console.log('+', test_result.toString().substring(0, 50));
    }
    console.log('Complete');
}

test()
