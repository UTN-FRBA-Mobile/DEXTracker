package com.github.utn.frba.mobile.dextracker.repository

import com.github.utn.frba.mobile.dextracker.data.Favourite
import com.github.utn.frba.mobile.dextracker.model.Session
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe

class InMemoryRepositoryTest : WordSpec() {
    init {
        lateinit var repo: InMemoryRepository
        beforeEach {
            repo = InMemoryRepository()

            repo.session = Session(
                dexToken = "123",
                userId = "U-123",
                pokedex = emptyList(),
                favourites = emptyList(),
            )
        }

        "merge" should {
            "add a favourite" {
                val expected = repo.session.copy(
                    favourites = listOf(
                        Favourite(
                            species = "bulbasaur",
                            gen = 1,
                            dexId = "UD-123",
                        )
                    )
                )

                repo.merge(
                    dexId = "UD-123",
                    favourites = listOf(
                        Favourite(
                            species = "bulbasaur",
                            gen = 1,
                            dexId = "UD-123",
                        ),
                    ),
                )

                repo.session shouldBe expected
            }

            "delete all the entries for the given id and add the new ones" {
                repo.session = repo.session.copy(
                    favourites = listOf(
                        Favourite(
                            species = "bulbasaur",
                            gen = 1,
                            dexId = "UD-123",
                        ),
                        Favourite(
                            species = "cyndaquil",
                            gen = 2,
                            dexId = "UD-456",
                        ),
                    ),
                )

                val expected = repo.session.copy(
                    favourites = listOf(
                        Favourite(
                            species = "cyndaquil",
                            gen = 2,
                            dexId = "UD-456",
                        ),
                        Favourite(
                            species = "charmander",
                            gen = 1,
                            dexId = "UD-123",
                        ),
                        Favourite(
                            species = "squirtle",
                            gen = 1,
                            dexId = "UD-123",
                        )
                    )
                )

                repo.merge(
                    dexId = "UD-123",
                    favourites = listOf(
                        Favourite(
                            species = "charmander",
                            gen = 1,
                            dexId = "UD-123",
                        ),
                        Favourite(
                            species = "squirtle",
                            gen = 1,
                            dexId = "UD-123",
                        ),
                    )
                )

                repo.session shouldBe expected
            }
        }
    }
}
