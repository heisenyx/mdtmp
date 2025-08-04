import React, { useState, useEffect } from 'react'
import { EditorContent, useEditor } from '@tiptap/react'
import { StarterKit } from '@syfxlin/tiptap-starter-kit'

const loadDraft = () => {
  try {
    return JSON.parse(localStorage.getItem('draft') || '{}')
  } catch {
    return {}
  }
}

export default function ContentEditor() {
    const draft = loadDraft()
    const [title, setTitle]   = useState(draft.t || '')
    const [author, setAuthor] = useState(draft.a || '')
    const [content, setContent] = useState(draft.c || `
        <p>This is a basic example of usage. Press / to see available commands. Click on Image to resize and align.</p>
        <img src="https://placehold.co/800x400/6A00F5/white" />
    `)

    const editor = useEditor({
        extensions: [
            StarterKit.configure({ slashMenu: true, floatMenu: false }),
        ],
        content: content,
        autofocus: true,
        onUpdate({ editor }) {
            setContent(editor.getJSON())
        },
    })

    useEffect(() => {
        localStorage.setItem(
        'draft',
        JSON.stringify({ t: title, a: author, c: content })
        )
    }, [title, author, content])

  return (
    <>
        <form className="editor-container">
            <input
                type="text"
                className="input-field"
                placeholder="Title"
                value={title}
                onChange={e => setTitle(e.target.value)}
            />
            <input
                type="text"
                className="input-field author"
                placeholder="Author"
                value={author}
                onChange={e => setAuthor(e.target.value)}
            />

            <EditorContent editor={editor}/>
        </form>

        <div className='buttons-container'>
        <button
            type="button"
            className='btn btn-secondary'
            // onClick={enhance}
        >
            Enhance
        </button>
        <button
            type="button"
            className='btn btn-primary'
            // onClick={publish}
        >
            Publish
        </button>
        </div>
    </>
  )
}