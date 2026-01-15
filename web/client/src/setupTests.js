/// <reference types="jest" />
/// <reference types="@testing-library/jest-dom" />

const { TextEncoder, TextDecoder } = require('util')
const { ReadableStream, TransformStream } = require('stream/web')
const { cleanup } = require('@testing-library/react')

global.TextEncoder = TextEncoder
global.TextDecoder = TextDecoder
global.ReadableStream = ReadableStream
global.TransformStream = TransformStream

require('@testing-library/jest-dom')
require('./testUtils/suppressConsoleErrors')

afterEach(() => {
  cleanup()
})

global.setImmediate = global.setImmediate || ((fn, ...args) => global.setTimeout(fn, 0, ...args))

Object.defineProperty(window, 'matchMedia', {
  writable: true,
  value: jest.fn().mockImplementation((query) => ({
    matches: false,
    media: query,
    onchange: null,
    addListener: jest.fn(),
    removeListener: jest.fn(),
    addEventListener: jest.fn(),
    removeEventListener: jest.fn(),
    dispatchEvent: jest.fn(),
  })),
})

global.ResizeObserver = jest.fn().mockImplementation(() => ({
  observe: jest.fn(),
  unobserve: jest.fn(),
  disconnect: jest.fn(),
}))
