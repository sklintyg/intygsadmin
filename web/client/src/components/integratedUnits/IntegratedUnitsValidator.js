export const COULD_NOT_FIND_UNIT = 'Kunde inte hitta någon enhet med detta HSA-id. Kontrollera att du matat in ett korrekt ID.'
export const COULD_NOT_DOWNLOAD_FILE = 'Filen kunde inte laddas ner på grund av ett tekniskt fel. Prova igen om en stund.'
export const TECHNICAL_ERROR = 'Sökningen kunde inte genomföras på grund av ett tekniskt fel. Prova igen om en stund.'

export const validateIntegratedUnit = (integratedUnitIdResult, errorMessage) => {
  if (errorMessage !== null) {
    return TECHNICAL_ERROR
  } else if (integratedUnitIdResult === null) {
    return COULD_NOT_FIND_UNIT
  } else {
    return undefined
  }
}

export const validateIntegratedUnitsFile = (errorMessage) => {
  if (errorMessage !== null) {
    return COULD_NOT_DOWNLOAD_FILE
  } else {
    return undefined
  }
}


