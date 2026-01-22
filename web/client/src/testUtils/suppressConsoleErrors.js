const originalError = console.error
const originalWarn = console.warn

beforeAll(() => {
  console.error = (...args) => {
    const message = args[0]
    if (
      typeof message === 'string' &&
      (message.includes('Not implemented: HTMLFormElement.prototype.submit') ||
        message.includes('Error: Not implemented: HTMLFormElement.prototype.submit') ||
        message.includes('act(...)'))
    ) {
      return
    }
    originalError.call(console, ...args)
  }

  console.warn = (...args) => {
    originalWarn.call(console, ...args)
  }
})

afterAll(() => {
  console.error = originalError
  console.warn = originalWarn
})

export {}
