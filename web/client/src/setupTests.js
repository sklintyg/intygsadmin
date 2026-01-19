/// <reference types="vitest/globals" />
/// <reference types="@testing-library/jest-dom" />

import { cleanup } from '@testing-library/react'
import '@testing-library/jest-dom'
import { afterEach, vi } from 'vitest'
import './testUtils/suppressConsoleErrors'

const { TextEncoder, TextDecoder } = await import('util')
const { ReadableStream, TransformStream } = await import('stream/web')

global.TextEncoder = TextEncoder
global.TextDecoder = TextDecoder
global.ReadableStream = ReadableStream
global.TransformStream = TransformStream

afterEach(() => {
  cleanup()
})

global.setImmediate = global.setImmediate || ((fn, ...args) => global.setTimeout(fn, 0, ...args))

Object.defineProperty(window, 'matchMedia', {
  writable: true,
  value: vi.fn().mockImplementation((query) => ({
    matches: false,
    media: query,
    onchange: null,
    addListener: vi.fn(),
    removeListener: vi.fn(),
    addEventListener: vi.fn(),
    removeEventListener: vi.fn(),
    dispatchEvent: vi.fn(),
  })),
})

global.ResizeObserver = vi.fn().mockImplementation(() => ({
  observe: vi.fn(),
  unobserve: vi.fn(),
  disconnect: vi.fn(),
}))
