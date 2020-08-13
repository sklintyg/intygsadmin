export const COULD_NOT_FIND_PRIVATE_PRACTITIONER = 'Kunde inte hitta någon privatläkare med detta HSA-id/ personnummer. Kontrollera att du matat in ett korrekt ID.'
export const COULD_NOT_DOWNLOAD_FILE = 'Filen kunde inte laddas ner på grund av ett tekniskt fel. Prova igen om en stund.'

export const validatePrivatePractitioner = (privatePractitionerIdResult, errorMessage) => {
  if (errorMessage !== null) {
    return errorMessage
  } else if (privatePractitionerIdResult === null) {
    return COULD_NOT_FIND_PRIVATE_PRACTITIONER
  } else {
    return undefined
  }
}

export const validatePrivatePractitionerFile = (errorMessage) => {
  if (errorMessage !== null) {
    return COULD_NOT_DOWNLOAD_FILE
  } else {
    return undefined
  }
}


