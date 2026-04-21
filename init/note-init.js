db.notes.deleteMany({ patientId: { $in: [1, 2, 3, 4, 5, 6, 7, 8] } });

db.notes.insertMany([
  // 1 - None -> 0 trigger
  {
    patientId: 1,
    note: "Patient reports good general health. Routine follow-up. No concerning symptom reported.",
    createdAt: new Date("2026-04-21T10:00:00Z")
  },

  // 2 - Borderline (>30, 2 triggers)
  {
    patientId: 2,
    note: "Patient reports dizziness during the last few days.",
    createdAt: new Date("2026-04-21T10:05:00Z")
  },
  {
    patientId: 2,
    note: "The patient is a smoker.",
    createdAt: new Date("2026-04-21T10:06:00Z")
  },

  // 3 - In Danger man <30 (3 triggers)
  {
    patientId: 3,
    note: "Patient reports dizziness after effort.",
    createdAt: new Date("2026-04-21T10:10:00Z")
  },
  {
    patientId: 3,
    note: "The patient is a smoker.",
    createdAt: new Date("2026-04-21T10:11:00Z")
  },
  {
    patientId: 3,
    note: "Lab results mention abnormal values.",
    createdAt: new Date("2026-04-21T10:12:00Z")
  },

  // 4 - Early onset woman <30 (7 triggers)
  {
    patientId: 4,
    note: "Lab results mention hemoglobin a1c and microalbumin.",
    createdAt: new Date("2026-04-21T10:15:00Z")
  },
  {
    patientId: 4,
    note: "Patient height and weight were recorded.",
    createdAt: new Date("2026-04-21T10:16:00Z")
  },
  {
    patientId: 4,
    note: "The patient is a smoker with abnormal cholesterol.",
    createdAt: new Date("2026-04-21T10:17:00Z")
  },
  {
    patientId: 4,
    note: "Patient reports dizziness and reaction.",
    createdAt: new Date("2026-04-21T10:18:00Z")
  },

  // 5 - Early onset man <30 (5 triggers)
  {
    patientId: 5,
    note: "Lab results mention hemoglobin a1c and microalbumin.",
    createdAt: new Date("2026-04-21T10:20:00Z")
  },
  {
    patientId: 5,
    note: "The patient is a smoker.",
    createdAt: new Date("2026-04-21T10:21:00Z")
  },
  {
    patientId: 5,
    note: "Patient reports dizziness and abnormal values.",
    createdAt: new Date("2026-04-21T10:22:00Z")
  },

  // 6 - In Danger woman <30 (4 triggers)
  {
    patientId: 6,
    note: "Patient reports dizziness.",
    createdAt: new Date("2026-04-21T10:25:00Z")
  },
  {
    patientId: 6,
    note: "The patient is a smoker.",
    createdAt: new Date("2026-04-21T10:26:00Z")
  },
  {
    patientId: 6,
    note: "Lab results show abnormal cholesterol.",
    createdAt: new Date("2026-04-21T10:27:00Z")
  },

  // 7 - In Danger >30 (6 triggers)
  {
    patientId: 7,
    note: "Lab results mention hemoglobin a1c and microalbumin.",
    createdAt: new Date("2026-04-21T10:30:00Z")
  },
  {
    patientId: 7,
    note: "Patient height and weight were recorded.",
    createdAt: new Date("2026-04-21T10:31:00Z")
  },
  {
    patientId: 7,
    note: "The patient is a smoker with abnormal cholesterol.",
    createdAt: new Date("2026-04-21T10:32:00Z")
  },

  // 8 - Early onset >30 (8 triggers)
  {
    patientId: 8,
    note: "Lab results mention hemoglobin a1c and microalbumin.",
    createdAt: new Date("2026-04-21T10:35:00Z")
  },
  {
    patientId: 8,
    note: "Patient height and weight were recorded.",
    createdAt: new Date("2026-04-21T10:36:00Z")
  },
  {
    patientId: 8,
    note: "The patient is a smoker with abnormal cholesterol.",
    createdAt: new Date("2026-04-21T10:37:00Z")
  },
  {
    patientId: 8,
    note: "Patient reports dizziness and reaction.",
    createdAt: new Date("2026-04-21T10:38:00Z")
  }
]);