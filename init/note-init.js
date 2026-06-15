db.notes.deleteMany({ patientId: { $in: [1, 2, 3, 4, 5, 6, 7, 8] } });

db.notes.insertMany([

  // 1 - Aucun risque -> 0 déclencheur
  {
    patientId: 1,
    note: "Le patient est en bonne santé générale. Visite de suivi sans anomalie.",
    createdAt: new Date("2026-04-21T10:00:00Z")
  },

  // 2 - Borderline (>30 ans, 2 déclencheurs)
  {
    patientId: 2,
    note: "Le patient présente des vertiges depuis quelques jours.",
    createdAt: new Date("2026-04-21T10:05:00Z")
  },
  {
    patientId: 2,
    note: "Le patient est fumeur.",
    createdAt: new Date("2026-04-21T10:06:00Z")
  },

  // 3 - In Danger homme <30 ans (3 déclencheurs)
  {
    patientId: 3,
    note: "Le patient présente des vertiges après un effort physique.",
    createdAt: new Date("2026-04-21T10:10:00Z")
  },
  {
    patientId: 3,
    note: "Le patient est fumeur.",
    createdAt: new Date("2026-04-21T10:11:00Z")
  },
  {
    patientId: 3,
    note: "Les analyses révèlent des résultats anormal.",
    createdAt: new Date("2026-04-21T10:12:00Z")
  },

  // 4 - Early Onset femme <30 ans (8 déclencheurs)
  {
    patientId: 4,
    note: "Les analyses mentionnent hemoglobin a1c et microalbumin.",
    createdAt: new Date("2026-04-21T10:15:00Z")
  },
  {
    patientId: 4,
    note: "La taille et le poids du patient ont été enregistrés.",
    createdAt: new Date("2026-04-21T10:16:00Z")
  },
  {
    patientId: 4,
    note: "La patiente est fumeuse avec un cholesterol anormal.",
    createdAt: new Date("2026-04-21T10:17:00Z")
  },
  {
    patientId: 4,
    note: "La patiente présente des vertiges ainsi qu'une reaction.",
    createdAt: new Date("2026-04-21T10:18:00Z")
  },

  // 5 - Early Onset homme <30 ans (5 déclencheurs)
  {
    patientId: 5,
    note: "Les analyses mentionnent hemoglobin a1c et microalbumin.",
    createdAt: new Date("2026-04-21T10:20:00Z")
  },
  {
    patientId: 5,
    note: "Le patient est fumeur.",
    createdAt: new Date("2026-04-21T10:21:00Z")
  },
  {
    patientId: 5,
    note: "Le patient présente des vertiges avec un résultat anormal.",
    createdAt: new Date("2026-04-21T10:22:00Z")
  },

  // 6 - In Danger femme <30 ans (4 déclencheurs)
  {
    patientId: 6,
    note: "La patiente présente des vertiges.",
    createdAt: new Date("2026-04-21T10:25:00Z")
  },
  {
    patientId: 6,
    note: "La patiente est fumeuse.",
    createdAt: new Date("2026-04-21T10:26:00Z")
  },
  {
    patientId: 6,
    note: "Les analyses révèlent un cholesterol anormal.",
    createdAt: new Date("2026-04-21T10:27:00Z")
  },

  // 7 - In Danger >30 ans (6 déclencheurs)
  {
    patientId: 7,
    note: "Les analyses mentionnent hemoglobin a1c et microalbumin.",
    createdAt: new Date("2026-04-21T10:30:00Z")
  },
  {
    patientId: 7,
    note: "La taille et le poids du patient ont été enregistrés.",
    createdAt: new Date("2026-04-21T10:31:00Z")
  },
  {
    patientId: 7,
    note: "Le patient est fumeur avec un cholesterol anormal.",
    createdAt: new Date("2026-04-21T10:32:00Z")
  },

  // 8 - Early Onset >30 ans (8 déclencheurs)
  {
    patientId: 8,
    note: "Les analyses mentionnent hemoglobin a1c et microalbumin.",
    createdAt: new Date("2026-04-21T10:35:00Z")
  },
  {
    patientId: 8,
    note: "La taille et le poids du patient ont été enregistrés.",
    createdAt: new Date("2026-04-21T10:36:00Z")
  },
  {
    patientId: 8,
    note: "Le patient est fumeur avec un cholesterol anormal.",
    createdAt: new Date("2026-04-21T10:37:00Z")
  },
  {
    patientId: 8,
    note: "Le patient présente des vertiges et une reaction.",
    createdAt: new Date("2026-04-21T10:38:00Z")
  }
]);